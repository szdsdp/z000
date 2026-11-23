package com.szd.z000.web.sap.pp.orderReport.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.*;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.security.ClientSecurity;
import com.szd.core.client.security.ClientUser;
import com.szd.core.client.util.ClientUtilDate;
import com.szd.core.client.util.ClientUtilString;
import com.szd.z000.base.orm.domain.pp.orderReport.OrderReportHeader;
import com.szd.z000.base.orm.domain.pp.orderReport.OrderReportItem;
import com.szd.z000.service.common.utils.Z000Util;
import com.szd.z000.web.sap.pp.orderReport.domain.OrderReportDataVO;
import com.szd.z000.web.sap.pp.orderReport.domain.OrderReportParamVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 生产订单报工-服务类
 */
@Service
public class OrderReportService {
    private static final Logger logger = LoggerFactory.getLogger(OrderReportService.class);

    @Resource
    private OrderReportHeaderService orderReportHeaderService;
    @Resource
    private OrderReportItemService orderReportItemService;
    @Resource
    private OrderReportSapService orderReportSapService;
    @Resource
    private ClientCore clientCore;
    @Resource
    private OrderReportService orderReportService;

    public List<OrderReportHeader> selectList(ClientRpParam param) {
        return orderReportHeaderService.selectList(param);
    }

    public OrderReportDataVO selectData(String reportId) {
        OrderReportDataVO dataVO = new OrderReportDataVO();
        OrderReportHeader header = orderReportHeaderService.selectById(reportId);
        List<OrderReportItem> itemList = orderReportItemService.selectList(reportId);
        dataVO.setHeader(header);
        dataVO.setItemList(itemList);
        return dataVO;
    }

    public ClientResult check(OrderReportParamVO paramVO) {
        ClientResult ret = new ClientResult();
        checkData(paramVO);
        ret.setStatus("S");
        ret.setMsg("检查通过");
        return ret;
    }

    private void checkData(OrderReportParamVO paramVO) {
        OrderReportDataVO data = paramVO.getData();
        OrderReportHeader header = data.getHeader();
        ClientResult clientResult = clientCore.wf.wfCheckBtn(header.getReportId(), ClientSecurity.getUserId(), "U", paramVO.getWfEvt().getOperate());
        if (clientResult != null && "E".equals(clientResult.getStatus())) {
            throw new ClientCustomException(clientResult.getMsg());
        }
        orderReportHeaderService.checkData(header);
    }

    public ClientWfReturn saveData(OrderReportParamVO paramVO) {
        ClientWfReturn clientWfReturn = new ClientWfReturn();
        if ("btn_delete".equals(paramVO.getWfEvt().getOperate())) {
            paramVO.setData(selectData(paramVO.getData().getHeader().getReportId()));
        } else {
            checkData(paramVO);
        }
        saveBussWfData(paramVO, clientWfReturn);
        ClientResult clientResult = orderReportSapService.genSapOrderReport(paramVO.getData().getHeader().getReportId());
        if (ClientUtilString.isNotBlank(clientResult.getStatus())) {
            if (CollectionUtils.isEmpty(clientWfReturn.getRetMsgBox())) {
                clientWfReturn.setRetMsgBox(new ArrayList<>());
            }
            clientWfReturn.getRetMsgBox().addAll(clientResult.getRetMsgBox());
        }
        return clientWfReturn;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveBussWfData(OrderReportParamVO paramVO, ClientWfReturn clientWfReturn) {
        saveBussData(paramVO);
        ClientWfReturn wfReturn = approval(paramVO.getData(), paramVO.getWfEvt());
        ClientWfBussBase wfBussBase = wfReturn.getWfBussBase();
        BeanUtils.copyProperties(wfReturn, clientWfReturn);
        clientWfReturn.setBussId(paramVO.getData().getHeader().getReportId());
        clientWfReturn.setBussDocId(paramVO.getData().getHeader().getBussDocId());
        if (!"E".equals(wfReturn.getRetCodeF())) {
            // 草稿状态的单据，作废时直接删除
            if ("btn_delete".equals(paramVO.getWfEvt().getOperate()) && "A".equals(paramVO.getData().getHeader().getBussStatus())) {
                deleteById(clientWfReturn.getBussId());
            } else {
                // 工作流状态转换为业务状态
                String bussStatus = Z000Util.getBussStatus(wfBussBase.getStatus(), wfBussBase.getRejectStatus());
                paramVO.getData().getHeader().setBussStatus(bussStatus);
                paramVO.getData().getHeader().setBussDocId(wfReturn.getBussDocId());
                orderReportHeaderService.updateStatusAndDocId(paramVO.getData().getHeader());
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveBussData(OrderReportParamVO paramVO) {
        try {
            OrderReportDataVO data = paramVO.getData();
            OrderReportHeader header = data.getHeader();
            if (StringUtils.isBlank(header.getReportId())) {
                String bussId = clientCore.tool.getNumSeq();
                header.setReportId(bussId);
                header.setBussId(bussId);
                header.setCreateBy(ClientSecurity.getUserId());
                header.setCreateTime(new Date());
                header.setBussStatus("A");
                Z000Util.saveBussBase(header);
                orderReportService.insert(data);
            } else {
                header.setUpdateBy(ClientSecurity.getUserId());
                header.setUpdateTime(new Date());
                Z000Util.saveBussBase(header);
                orderReportService.update(data, paramVO.getWfEvt().getOperate());
            }
        } catch (Exception e) {
            logger.error("保存失败", e);
            throw new ClientCustomException("保存失败:" + e.getMessage());
        }
    }

    public ClientWfReturn approval(OrderReportDataVO data, ClientWfParamEvt wfEvt) {
        ClientWfReturn wfReturn = new ClientWfReturn();
        try {
            OrderReportHeader header = data.getHeader();
            String bussId = header.getReportId();
            ClientWfParamBuss wfParamBuss = Z000Util.setWfParamBuss(header, bussId);
            ClientWfParam wfParam = new ClientWfParam(wfEvt, wfParamBuss);
            wfReturn = clientCore.wf.wfCall(wfParam);
        } catch (Exception e) {
            wfReturn.setRetCodeF("E");
            wfReturn.setRetMsg(e.getMessage());
        }
        return wfReturn;
    }

    private void deleteById(String reportId) {
        orderReportHeaderService.deleteById(reportId);
        orderReportItemService.deleteByOrderReportId(reportId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insert(OrderReportDataVO data) {
        OrderReportHeader header = data.getHeader();
        orderReportHeaderService.insert(header);
        orderReportItemService.save(header.getReportId(), data.getItemList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(OrderReportDataVO data, String operate) {
        String reportId = data.getHeader().getReportId();
        OrderReportHeader headerOld = orderReportHeaderService.selectObjById(reportId);
        orderReportHeaderService.update(data.getHeader());
        // 查询历史行项目（在save覆盖前查询）
        List<OrderReportItem> itemListOld = orderReportItemService.selectList(reportId);
        orderReportItemService.save(reportId, data.getItemList());
        saveLog(data, headerOld, itemListOld, operate);
    }

    /**
     * 从生产订单抽取的工序行创建报工单(A状态草稿)
     */
    @Transactional(rollbackFor = Exception.class)
    public String createFromOrderExtract(OrderReportParamVO paramVO) {
        OrderReportDataVO data = paramVO.getData();
        OrderReportHeader header = data.getHeader();
        // 生成单据ID
        String bussId = clientCore.tool.getNumSeq();
        header.setReportId(bussId);
        header.setBussId(bussId);
        header.setCreateBy(ClientSecurity.getUserId());
        header.setCreateTime(new Date());
        header.setBussStatus("A");
        // 从当前登录用户获取公司和成本中心
        ClientUser user = ClientSecurity.getLoginUser().getUser();
        header.setCmpy(user.getCmpy());
        header.setCstc(user.getCstc());
        header.setTitle("生产报工_" + ClientUtilDate.getDate("yyyy-MM-dd", new Date()));
        header.setBussDate(new Date());
        // 设置行项目号
        if (!CollectionUtils.isEmpty(data.getItemList())) {
            for (int i = 0; i < data.getItemList().size(); i++) {
                data.getItemList().get(i).setItemNo(i + 1);
            }
        }
        Z000Util.saveBussBase(header);
        orderReportService.insert(data);
        return bussId;
    }

    private void saveLog(OrderReportDataVO data, OrderReportHeader headerOld, List<OrderReportItem> itemListOld, String operate) {
        OrderReportHeader header = data.getHeader();
        clientCore.tool.clogSaveA(header.getReportId(), "z000_pp_order_report_header", operate, header, headerOld);
        clientCore.tool.clogSaveB(header.getReportId(), "itemNo", "z000_pp_order_report_item", operate, data.getItemList(), itemListOld);
    }
}

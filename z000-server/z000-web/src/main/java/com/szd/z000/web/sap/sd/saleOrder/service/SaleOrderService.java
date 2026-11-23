package com.szd.z000.web.sap.sd.saleOrder.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.*;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.security.ClientSecurity;
import com.szd.core.client.util.ClientUtilDate;
import com.szd.core.client.util.ClientUtilString;
import com.szd.z000.base.orm.domain.sd.saleOrder.SaleOrderHeader;
import com.szd.z000.base.orm.domain.sd.saleOrder.SaleOrderItem;
import com.szd.z000.base.orm.domain.sd.saleOrder.SaleOrderSched;
import com.szd.z000.service.common.utils.Z000Util;
import com.szd.z000.web.sap.sd.saleOrder.domain.SaleOrderDataVO;
import com.szd.z000.web.sap.sd.saleOrder.domain.SaleOrderParamVO;
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
 * 销售订单创建-服务类
 */
@Service
public class SaleOrderService {
    private static final Logger logger = LoggerFactory.getLogger(SaleOrderService.class);

    @Resource
    private SaleOrderHeaderService saleOrderHeaderService;
    @Resource
    private SaleOrderItemService saleOrderItemService;
    @Resource
    private SaleOrderSchedService saleOrderSchedService;
    @Resource
    private SaleOrderSapService saleOrderSapService;
    @Resource
    private ClientCore clientCore;
    @Resource
    private SaleOrderService saleOrderService;

    /**
     * 列表查询
     */
    public List<SaleOrderHeader> selectList(ClientRpParam param) {
        return saleOrderHeaderService.selectList(param);
    }

    /**
     * 查询单据详情
     */
    public SaleOrderDataVO selectData(String orderId) {
        SaleOrderDataVO dataVO = new SaleOrderDataVO();
        SaleOrderHeader header = saleOrderHeaderService.selectById(orderId);
        List<SaleOrderItem> itemList = saleOrderItemService.selectList(orderId);
        List<SaleOrderSched> schedList = saleOrderSchedService.selectList(orderId);
        dataVO.setHeader(header);
        dataVO.setItemList(itemList);
        dataVO.setSchedList(schedList);
        return dataVO;
    }

    /**
     * 数据检查
     */
    public ClientResult check(SaleOrderParamVO paramVO) {
        ClientResult ret = new ClientResult();
        checkData(paramVO);
        ret.setStatus("S");
        ret.setMsg("检查通过");
        return ret;
    }

    /**
     * 业务数据及工作流按钮权限校验
     */
    private void checkData(SaleOrderParamVO paramVO) {
        SaleOrderDataVO data = paramVO.getData();
        SaleOrderHeader header = data.getHeader();
        ClientResult clientResult = clientCore.wf.wfCheckBtn(header.getOrderId(), ClientSecurity.getUserId(), "U", paramVO.getWfEvt().getOperate());
        if (clientResult != null && "E".equals(clientResult.getStatus())) {
            throw new ClientCustomException(clientResult.getMsg());
        }
        saleOrderHeaderService.checkData(header);
    }

    /**
     * 数据保存
     */
    public ClientWfReturn saveData(SaleOrderParamVO paramVO) {
        ClientWfReturn clientWfReturn = new ClientWfReturn();
        if ("btn_delete".equals(paramVO.getWfEvt().getOperate())) {
            paramVO.setData(selectData(paramVO.getData().getHeader().getOrderId()));
        } else {
            checkData(paramVO);
        }
        saveBussWfData(paramVO, clientWfReturn);
        ClientResult clientResult = saleOrderSapService.genSapSaleOrder(paramVO.getData().getHeader().getOrderId());
        if (ClientUtilString.isNotBlank(clientResult.getStatus())) {
            if (CollectionUtils.isEmpty(clientWfReturn.getRetMsgBox())) {
                clientWfReturn.setRetMsgBox(new ArrayList<>());
            }
            clientWfReturn.getRetMsgBox().addAll(clientResult.getRetMsgBox());
        }
        return clientWfReturn;
    }

    /**
     * 保存业务数据及执行工作流审批
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveBussWfData(SaleOrderParamVO paramVO, ClientWfReturn clientWfReturn) {
        saveBussData(paramVO);
        ClientWfReturn wfReturn = approval(paramVO.getData(), paramVO.getWfEvt());
        ClientWfBussBase wfBussBase = wfReturn.getWfBussBase();
        BeanUtils.copyProperties(wfReturn, clientWfReturn);
        clientWfReturn.setBussId(paramVO.getData().getHeader().getOrderId());
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
                saleOrderHeaderService.updateStatusAndDocId(paramVO.getData().getHeader());
            }
        }
    }

    /**
     * 保存草稿
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveBussData(SaleOrderParamVO paramVO) {
        try {
            SaleOrderDataVO data = paramVO.getData();
            SaleOrderHeader header = data.getHeader();
            if (StringUtils.isBlank(header.getOrderId())) {
                String bussId = clientCore.tool.getNumSeq();
                header.setOrderId(bussId);
                header.setBussId(bussId);
                header.setCreateBy(ClientSecurity.getUserId());
                header.setCreateTime(new Date());
                header.setBussStatus("A");
                Z000Util.saveBussBase(header);
                saleOrderService.insert(data);
            } else {
                header.setUpdateBy(ClientSecurity.getUserId());
                header.setUpdateTime(new Date());
                Z000Util.saveBussBase(header);
                saleOrderService.update(data, paramVO.getWfEvt().getOperate());
            }
        } catch (Exception e) {
            logger.error("保存失败", e);
            throw new ClientCustomException("保存失败:" + e.getMessage());
        }
    }

    /**
     * 流程审批
     */
    public ClientWfReturn approval(SaleOrderDataVO data, ClientWfParamEvt wfEvt) {
        ClientWfReturn wfReturn = new ClientWfReturn();
        try {
            SaleOrderHeader header = data.getHeader();
            String bussId = header.getOrderId();
            ClientWfParamBuss wfParamBuss = Z000Util.setWfParamBuss(header, bussId);
            ClientWfParam wfParam = new ClientWfParam(wfEvt, wfParamBuss);
            wfReturn = clientCore.wf.wfCall(wfParam);
        } catch (Exception e) {
            wfReturn.setRetCodeF("E");
            wfReturn.setRetMsg(e.getMessage());
        }
        return wfReturn;
    }

    /**
     * 删除单据
     */
    private void deleteById(String orderId) {
        saleOrderHeaderService.deleteById(orderId);
        saleOrderItemService.deleteByOrderId(orderId);
        saleOrderSchedService.deleteByOrderId(orderId);
    }

    /**
     * 业务数据新增
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(SaleOrderDataVO data) {
        SaleOrderHeader header = data.getHeader();
        saleOrderHeaderService.insert(header);
        saleOrderItemService.save(header.getOrderId(), data.getItemList());
        saleOrderSchedService.save(header.getOrderId(), data.getSchedList());
    }

    /**
     * 业务数据更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(SaleOrderDataVO data, String operate) {
        String orderId = data.getHeader().getOrderId();
        // 查询历史数据（在save覆盖前查询）
        SaleOrderHeader headerOld = saleOrderHeaderService.selectObjById(orderId);
        List<SaleOrderItem> itemListOld = saleOrderItemService.selectList(orderId);
        List<SaleOrderSched> schedListOld = saleOrderSchedService.selectList(orderId);
        // 更新
        saleOrderHeaderService.update(data.getHeader());
        saleOrderItemService.save(orderId, data.getItemList());
        saleOrderSchedService.save(orderId, data.getSchedList());
        // 保存修改日志
        saveLog(data, headerOld, itemListOld, schedListOld, operate);
    }

    /**
     * 记录修改日志
     */
    private void saveLog(SaleOrderDataVO data, SaleOrderHeader headerOld, List<SaleOrderItem> itemListOld, List<SaleOrderSched> schedListOld, String operate) {
        SaleOrderHeader header = data.getHeader();
        clientCore.tool.clogSaveA(header.getOrderId(), "z000_sd_sale_order_header", operate, header, headerOld);
        clientCore.tool.clogSaveB(header.getOrderId(), "itemNo", "z000_sd_sale_order_item", operate, data.getItemList(), itemListOld);
        clientCore.tool.clogSaveB(header.getOrderId(), "schedNo", "z000_sd_sale_order_sched", operate, data.getSchedList(), schedListOld);
    }
}

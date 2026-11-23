package com.szd.z000.web.sap.pp.orderCreate.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.*;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.security.ClientSecurity;
import com.szd.core.client.util.ClientUtilString;
import com.szd.z000.base.orm.domain.pp.orderCreate.OrderCreateHeader;
import com.szd.z000.service.common.utils.Z000Util;
import com.szd.z000.web.sap.pp.orderCreate.domain.OrderCreateDataVO;
import com.szd.z000.web.sap.pp.orderCreate.domain.OrderCreateParamVO;
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
 * 生产订单创建-服务类
 */
@Service
public class OrderCreateService {
    private static final Logger logger = LoggerFactory.getLogger(OrderCreateService.class);

    @Resource
    private OrderCreateHeaderService orderCreateHeaderService;
    @Resource
    private OrderCreateSapService orderCreateSapService;
    @Resource
    private ClientCore clientCore;
    @Resource
    private OrderCreateService orderCreateService;

    public List<OrderCreateHeader> selectList(ClientRpParam param) {
        return orderCreateHeaderService.selectList(param);
    }

    public OrderCreateDataVO selectData(String applyId) {
        OrderCreateDataVO dataVO = new OrderCreateDataVO();
        OrderCreateHeader header = orderCreateHeaderService.selectById(applyId);
        dataVO.setHeader(header);
        return dataVO;
    }

    public ClientResult check(OrderCreateParamVO paramVO) {
        ClientResult ret = new ClientResult();
        checkData(paramVO);
        ret.setStatus("S");
        ret.setMsg("检查通过");
        return ret;
    }

    private void checkData(OrderCreateParamVO paramVO) {
        OrderCreateDataVO data = paramVO.getData();
        OrderCreateHeader header = data.getHeader();
        ClientResult clientResult = clientCore.wf.wfCheckBtn(header.getApplyId(), ClientSecurity.getUserId(), "U", paramVO.getWfEvt().getOperate());
        if (clientResult != null && "E".equals(clientResult.getStatus())) {
            throw new ClientCustomException(clientResult.getMsg());
        }
        orderCreateHeaderService.checkData(header);
    }

    public ClientWfReturn saveData(OrderCreateParamVO paramVO) {
        ClientWfReturn clientWfReturn = new ClientWfReturn();
        if ("btn_delete".equals(paramVO.getWfEvt().getOperate())) {
            paramVO.setData(selectData(paramVO.getData().getHeader().getApplyId()));
        } else {
            checkData(paramVO);
        }
        saveBussWfData(paramVO, clientWfReturn);
        ClientResult clientResult = orderCreateSapService.genSapOrderCreate(paramVO.getData().getHeader().getApplyId());
        if (ClientUtilString.isNotBlank(clientResult.getStatus())) {
            if (CollectionUtils.isEmpty(clientWfReturn.getRetMsgBox())) {
                clientWfReturn.setRetMsgBox(new ArrayList<>());
            }
            clientWfReturn.getRetMsgBox().addAll(clientResult.getRetMsgBox());
        }
        return clientWfReturn;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveBussWfData(OrderCreateParamVO paramVO, ClientWfReturn clientWfReturn) {
        saveBussData(paramVO);
        ClientWfReturn wfReturn = approval(paramVO.getData(), paramVO.getWfEvt());
        ClientWfBussBase wfBussBase = wfReturn.getWfBussBase();
        BeanUtils.copyProperties(wfReturn, clientWfReturn);
        clientWfReturn.setBussId(paramVO.getData().getHeader().getApplyId());
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
                orderCreateHeaderService.updateStatusAndDocId(paramVO.getData().getHeader());
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveBussData(OrderCreateParamVO paramVO) {
        try {
            OrderCreateDataVO data = paramVO.getData();
            OrderCreateHeader header = data.getHeader();
            if (StringUtils.isBlank(header.getApplyId())) {
                String bussId = clientCore.tool.getNumSeq();
                header.setApplyId(bussId);
                header.setBussId(bussId);
                header.setCreateBy(ClientSecurity.getUserId());
                header.setCreateTime(new Date());
                header.setBussStatus("A");
                Z000Util.saveBussBase(header);
                orderCreateService.insert(data);
            } else {
                header.setUpdateBy(ClientSecurity.getUserId());
                header.setUpdateTime(new Date());
                Z000Util.saveBussBase(header);
                orderCreateService.update(data, paramVO.getWfEvt().getOperate());
            }
        } catch (Exception e) {
            logger.error("保存失败", e);
            throw new ClientCustomException("保存失败:" + e.getMessage());
        }
    }

    public ClientWfReturn approval(OrderCreateDataVO data, ClientWfParamEvt wfEvt) {
        ClientWfReturn wfReturn = new ClientWfReturn();
        try {
            OrderCreateHeader header = data.getHeader();
            String bussId = header.getApplyId();
            ClientWfParamBuss wfParamBuss = Z000Util.setWfParamBuss(header, bussId);
            ClientWfParam wfParam = new ClientWfParam(wfEvt, wfParamBuss);
            wfReturn = clientCore.wf.wfCall(wfParam);
        } catch (Exception e) {
            wfReturn.setRetCodeF("E");
            wfReturn.setRetMsg(e.getMessage());
        }
        return wfReturn;
    }

    private void deleteById(String applyId) {
        orderCreateHeaderService.deleteById(applyId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insert(OrderCreateDataVO data) {
        orderCreateHeaderService.insert(data.getHeader());
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(OrderCreateDataVO data, String operate) {
        String applyId = data.getHeader().getApplyId();
        OrderCreateHeader headerOld = orderCreateHeaderService.selectObjById(applyId);
        orderCreateHeaderService.update(data.getHeader());
        saveLog(data, headerOld, operate);
    }

    private void saveLog(OrderCreateDataVO data, OrderCreateHeader headerOld, String operate) {
        OrderCreateHeader header = data.getHeader();
        clientCore.tool.clogSaveA(header.getApplyId(), "z000_pp_order_create_header", operate, header, headerOld);
    }
}

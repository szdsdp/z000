package com.szd.z000.common.sap.domain.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientResultMsgBox;
import com.szd.z000.base.common.constant.SapConstant;
import com.szd.z000.base.orm.mapper.pp.orderCreate.OrderCreateHeaderMapper;
import com.szd.z000.common.sap.domain.orderCreate.SapOrderCreateCtr;
import com.szd.z000.common.sap.domain.orderCreate.SapOrderCreateCtrRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * SAP生产订单创建接口服务类
 */
@Service
public class SapOrderCreateCreateService {
    private static final Logger logger = LoggerFactory.getLogger(SapOrderCreateCreateService.class);
    @Resource
    private OrderCreateHeaderMapper orderCreateHeaderMapper;
    @Resource
    private ClientCore clientCore;

    /***
     * 创建生产订单
     */
    @Transactional(rollbackFor = Exception.class)
    public ClientResult createOrderCreate(SapOrderCreateCtr sapOrderCreateCtr, String applyId) {
        ClientResult clientResult = new ClientResult();
        // 系统标识
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDPP001);
        // 调用接口
        SapOrderCreateCtrRet ret = null;
        try {
            ret = clientCore.inf.callSapXml(clientDestination, sapOrderCreateCtr, SapOrderCreateCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP创建生产订单接口错误:", e);
            clientResult.setStatus("E");
            clientResult.setMsg("创建生产订单接口错误:" + e.getMessage());
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP创建生产订单接口错误," + e.getMessage());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        }
        if (!ObjectUtils.isEmpty(ret)) {
            String retMessage;
            String retStatus;
            if ("S".equals(ret.getZstau())) {
                orderCreateHeaderMapper.updateSapRet(applyId, ret.getAufnr(), truncate(ret.getZmess()), clientDestination.getMessId());
                retStatus = "S";
                retMessage = "SAP创建生产订单成功," + ret.getAufnr();
            } else {
                retStatus = "E";
                retMessage = "SAP创建生产订单失败," + ret.getZmess();
            }
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox(retStatus, retMessage);
            clientResult.setStatus(retStatus);
            clientResult.setDocId(ret.getAufnr());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        } else {
            clientResult.setStatus("E");
            clientResult.setMsg("创建生产订单接口错误");
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP创建生产订单接口错误");
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        }
    }

    public String truncate(String input) {
        if (input == null) {
            return null;
        }
        if (input.length() <= 200) {
            return input;
        }
        return input.substring(0, 200);
    }
}

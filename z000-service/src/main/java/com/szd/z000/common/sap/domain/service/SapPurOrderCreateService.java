package com.szd.z000.common.sap.domain.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientResultMsgBox;
import com.szd.z000.base.common.constant.SapConstant;
import com.szd.z000.base.orm.mapper.mm.purOrder.PurOrderHeaderMapper;
import com.szd.z000.common.sap.domain.purOrder.SapPurOrderCtr;
import com.szd.z000.common.sap.domain.purOrder.SapPurOrderCtrRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * SAP采购订单接口服务类
 */
@Service
public class SapPurOrderCreateService {
    private static final Logger logger = LoggerFactory.getLogger(SapPurOrderCreateService.class);
    @Resource
    private PurOrderHeaderMapper purOrderHeaderMapper;
    @Resource
    private ClientCore clientCore;

    /***
     * 创建采购订单
     */
    @Transactional(rollbackFor = Exception.class)
    public ClientResult createPurOrder(SapPurOrderCtr sapPurOrderCtr, String orderId) {
        ClientResult clientResult = new ClientResult();
        // 系统标识
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDMM001);
        // 调用接口
        SapPurOrderCtrRet ret = null;
        try {
            ret = clientCore.inf.callSapXml(clientDestination, sapPurOrderCtr, SapPurOrderCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP创建采购订单接口错误:", e);
            clientResult.setStatus("E");
            clientResult.setMsg("创建采购订单接口错误:" + e.getMessage());
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP创建采购订单接口错误," + e.getMessage());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        }
        if (!ObjectUtils.isEmpty(ret)) {
            purOrderHeaderMapper.updateCgpzh(orderId, ret.getEbeln(), truncate(ret.getZmess()), clientDestination.getMessId());
            String retMessage;
            String retStatus;
            if ("S".equals(ret.getZstau())) {
                retStatus = "S";
                retMessage = "SAP创建采购订单成功," + ret.getEbeln();
            } else {
                retStatus = "E";
                retMessage = "SAP创建采购订单失败," + ret.getZmess();
            }
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox(retStatus, retMessage);
            clientResult.setStatus(retStatus);
            clientResult.setDocId(ret.getEbeln());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        } else {
            clientResult.setStatus("E");
            clientResult.setMsg("创建采购订单接口错误");
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP创建采购订单接口错误");
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

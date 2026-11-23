package com.szd.z000.common.sap.domain.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientResultMsgBox;
import com.szd.z000.base.common.constant.SapConstant;
import com.szd.z000.base.orm.mapper.sd.saleOrder.SaleOrderHeaderMapper;
import com.szd.z000.common.sap.domain.saleOrder.SapSaleOrderCtr;
import com.szd.z000.common.sap.domain.saleOrder.SapSaleOrderCtrRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * SAP销售订单创建接口服务类
 */
@Service
public class SapSaleOrderCreateService {
    private static final Logger logger = LoggerFactory.getLogger(SapSaleOrderCreateService.class);
    @Resource
    private SaleOrderHeaderMapper saleOrderHeaderMapper;
    @Resource
    private ClientCore clientCore;

    /***
     * 创建销售订单
     */
    @Transactional(rollbackFor = Exception.class)
    public ClientResult createSaleOrder(SapSaleOrderCtr sapSaleOrderCtr, String orderId) {
        ClientResult clientResult = new ClientResult();
        // 系统标识
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDSD001);
        // 调用接口
        SapSaleOrderCtrRet ret = null;
        try {
            ret = clientCore.inf.callSapXml(clientDestination, sapSaleOrderCtr, SapSaleOrderCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP创建销售订单接口错误:", e);
            clientResult.setStatus("E");
            clientResult.setMsg("创建销售订单接口错误:" + e.getMessage());
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP创建销售订单接口错误," + e.getMessage());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        }
        if (!ObjectUtils.isEmpty(ret)) {
            String retMessage;
            String retStatus;
            if ("S".equals(ret.getZstau())) {
                saleOrderHeaderMapper.updateSapRet(orderId, ret.getVbeln(), truncate(ret.getZmess()), clientDestination.getMessId());
                retStatus = "S";
                retMessage = "SAP创建销售订单成功," + ret.getVbeln();
            } else {
                retStatus = "E";
                retMessage = "SAP创建销售订单失败," + ret.getZmess();
            }
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox(retStatus, retMessage);
            clientResult.setStatus(retStatus);
            clientResult.setDocId(ret.getVbeln());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        } else {
            clientResult.setStatus("E");
            clientResult.setMsg("创建销售订单接口错误");
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP创建销售订单接口错误");
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

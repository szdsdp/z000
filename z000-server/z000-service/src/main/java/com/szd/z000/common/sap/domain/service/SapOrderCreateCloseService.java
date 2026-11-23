package com.szd.z000.common.sap.domain.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientResultMsgBox;
import com.szd.z000.base.common.constant.SapConstant;
import com.szd.z000.common.sap.domain.orderCreate.SapOrderCreateCloseCtr;
import com.szd.z000.common.sap.domain.orderCreate.SapOrderCreateCloseCtrRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * SAP生产订单关闭接口服务
 */
@Service
public class SapOrderCreateCloseService {
    private static final Logger logger = LoggerFactory.getLogger(SapOrderCreateCloseService.class);
    @Resource
    private ClientCore clientCore;

    public ClientResult close(SapOrderCreateCloseCtr sapCtr) {
        ClientResult clientResult = new ClientResult();
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDPP004);
        String action = "R".equals(sapCtr.getHeader().getZtype()) ? "取消关闭" : "关闭";
        try {
            SapOrderCreateCloseCtrRet ret = clientCore.inf.callSapXml(clientDestination, sapCtr, SapOrderCreateCloseCtrRet.class);
            if (ObjectUtils.isEmpty(ret)) {
                clientResult.setStatus("E");
                clientResult.setMsg(action + "生产订单接口错误");
                clientResult.getRetMsgBox().add(new ClientResultMsgBox("E", "SAP" + action + "生产订单接口错误"));
                return clientResult;
            }
            String retStatus, retMessage;
            if ("S".equals(ret.getZstau())) {
                retStatus = "S";
                retMessage = "SAP" + action + "生产订单成功";
            } else {
                retStatus = "E";
                retMessage = "SAP" + action + "生产订单失败," + ret.getZmess();
            }
            clientResult.setStatus(retStatus);
            clientResult.setMsg(retMessage);
            clientResult.getRetMsgBox().add(new ClientResultMsgBox(retStatus, retMessage));
            return clientResult;
        } catch (Exception e) {
            logger.error("调用SAP{}生产订单接口错误:", action, e);
            clientResult.setStatus("E");
            clientResult.setMsg(action + "生产订单接口错误:" + e.getMessage());
            clientResult.getRetMsgBox().add(new ClientResultMsgBox("E", "SAP" + action + "生产订单接口错误," + e.getMessage()));
            return clientResult;
        }
    }
}

package com.szd.z000.common.sap.domain.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientResultMsgBox;
import com.szd.z000.base.common.constant.SapConstant;
import com.szd.z000.common.sap.domain.orderCreate.SapOrderCreateReleaseCtr;
import com.szd.z000.common.sap.domain.orderCreate.SapOrderCreateReleaseCtrRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * SAP生产订单下达接口服务类
 */
@Service
public class SapOrderCreateReleaseService {
    private static final Logger logger = LoggerFactory.getLogger(SapOrderCreateReleaseService.class);
    @Resource
    private ClientCore clientCore;

    /**
     * 下达生产订单
     */
    public ClientResult release(SapOrderCreateReleaseCtr sapCtr, String applyId) {
        ClientResult clientResult = new ClientResult();
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDPP002);
        try {
            SapOrderCreateReleaseCtrRet ret = clientCore.inf.callSapXml(clientDestination, sapCtr, SapOrderCreateReleaseCtrRet.class);
            if (ObjectUtils.isEmpty(ret)) {
                clientResult.setStatus("E");
                clientResult.setMsg("下达生产订单接口错误");
                clientResult.getRetMsgBox().add(new ClientResultMsgBox("E", "SAP下达生产订单接口错误"));
                return clientResult;
            }
            String retStatus;
            String retMessage;
            if ("S".equals(ret.getZstau())) {
                retStatus = "S";
                retMessage = "SAP下达生产订单成功";
            } else {
                retStatus = "E";
                retMessage = "SAP下达生产订单失败," + ret.getZmess();
            }
            clientResult.setStatus(retStatus);
            clientResult.setMsg(retMessage);
            clientResult.getRetMsgBox().add(new ClientResultMsgBox(retStatus, retMessage));
            return clientResult;
        } catch (Exception e) {
            logger.error("调用SAP下达生产订单接口错误:", e);
            clientResult.setStatus("E");
            clientResult.setMsg("下达生产订单接口错误:" + e.getMessage());
            clientResult.getRetMsgBox().add(new ClientResultMsgBox("E", "SAP下达生产订单接口错误," + e.getMessage()));
            return clientResult;
        }
    }
}

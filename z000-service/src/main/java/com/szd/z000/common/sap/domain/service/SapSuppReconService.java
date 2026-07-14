package com.szd.z000.common.sap.domain.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.domain.common.ClientMdmReqCommon;
import com.szd.z000.base.common.constant.SapConstant;
import com.szd.z000.common.sap.domain.suppRecon.SapSuppReconCtrRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * 供应商对账查询-SAP接口服务
 */
@Service
public class SapSuppReconService {
    private static final Logger logger = LoggerFactory.getLogger(SapSuppReconService.class);
    @Resource
    private ClientCore clientCore;

    /**
     * 调用SAP查询供应商对账数据
     */
    public SapSuppReconCtrRet callSap(ClientMdmReqCommon req) {
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDMM090);
        SapSuppReconCtrRet ret;
        try {
            ret = clientCore.inf.callSapXml(clientDestination, req, SapSuppReconCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP供应商对账查询接口错误:", e);
            return null;
        }
        if (ObjectUtils.isEmpty(ret)) {
            return null;
        }
        return ret;
    }
}

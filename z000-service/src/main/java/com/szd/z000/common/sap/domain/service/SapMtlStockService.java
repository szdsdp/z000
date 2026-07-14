package com.szd.z000.common.sap.domain.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.domain.common.ClientMdmReqCommon;
import com.szd.z000.base.common.constant.SapConstant;
import com.szd.z000.common.sap.domain.mtlStock.SapMtlStockCtrRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * 物料库存查询-SAP接口服务
 */
@Service
public class SapMtlStockService {
    private static final Logger logger = LoggerFactory.getLogger(SapMtlStockService.class);
    @Resource
    private ClientCore clientCore;

    /**
     * 调用SAP查询物料库存
     */
    public SapMtlStockCtrRet callSap(ClientMdmReqCommon req) {
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDMM040);
        SapMtlStockCtrRet ret;
        try {
            ret = clientCore.inf.callSapXml(clientDestination, req, SapMtlStockCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP物料库存查询接口错误:", e);
            return null;
        }
        if (ObjectUtils.isEmpty(ret)) {
            return null;
        }
        return ret;
    }
}

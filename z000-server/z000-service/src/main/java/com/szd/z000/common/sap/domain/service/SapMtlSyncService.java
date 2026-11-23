package com.szd.z000.common.sap.domain.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.domain.common.ClientMdmReqCommon;
import com.szd.z000.base.common.constant.SapConstant;
import com.szd.z000.common.sap.domain.mtl.SapMtlCtrRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * 物料主数据同步-SAP接口服务
 */
@Service
public class SapMtlSyncService {
    private static final Logger logger = LoggerFactory.getLogger(SapMtlSyncService.class);
    @Resource
    private ClientCore clientCore;

    /**
     * 调用SAP查询物料主数据
     */
    public SapMtlCtrRet callSap(ClientMdmReqCommon req) {
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDMM010);
        SapMtlCtrRet ret;
        try {
            ret = clientCore.inf.callSapXml(clientDestination, req, SapMtlCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP物料主数据同步接口错误:", e);
            return null;
        }
        if (ObjectUtils.isEmpty(ret)) {
            return null;
        }
        return ret;
    }
}

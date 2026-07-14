package com.szd.z000.common.sap.domain.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.domain.common.ClientMdmReqCommon;
import com.szd.z000.base.common.constant.SapConstant;
import com.szd.z000.common.sap.domain.orderExtract.SapOrderExtractCtrRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * 生产订单抽取-SAP接口服务
 */
@Service
public class SapOrderExtractSyncService {
    private static final Logger logger = LoggerFactory.getLogger(SapOrderExtractSyncService.class);
    @Resource
    private ClientCore clientCore;

    public SapOrderExtractCtrRet callSap(ClientMdmReqCommon req) {
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDPP005);
        try {
            SapOrderExtractCtrRet sapOrderExtractCtrRet = clientCore.inf.callSapXml(clientDestination, req, SapOrderExtractCtrRet.class);
            return sapOrderExtractCtrRet;
        } catch (Exception e) {
            logger.error("调用SAP生产订单抽取接口错误:", e);
            return null;
        }
    }
}

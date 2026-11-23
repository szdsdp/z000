package com.szd.z000.common.sap.domain.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.domain.common.ClientMdmReqCommon;
import com.szd.z000.base.common.constant.SapConstant;
import com.szd.z000.common.sap.domain.payClear.SapPayClearCtrRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * 客商未清项抽取-SAP接口服务
 */
@Service
public class SapPayClearService {
    private static final Logger logger = LoggerFactory.getLogger(SapPayClearService.class);
    @Resource
    private ClientCore clientCore;

    /**
     * 调用SAP查询客商未清项数据
     */
    public SapPayClearCtrRet callSap(ClientMdmReqCommon req) {
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDFI014);
        SapPayClearCtrRet ret;
        try {
            ret = clientCore.inf.callSapXml(clientDestination, req, SapPayClearCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP客商未清项抽取接口错误:", e);
            return null;
        }
        if (ObjectUtils.isEmpty(ret)) {
            return null;
        }
        return ret;
    }
}

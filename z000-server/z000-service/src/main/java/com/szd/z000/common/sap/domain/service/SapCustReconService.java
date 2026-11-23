package com.szd.z000.common.sap.domain.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.domain.common.ClientMdmReqCommon;
import com.szd.z000.base.common.constant.SapConstant;
import com.szd.z000.common.sap.domain.custRecon.SapCustReconCtrRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * 客商对账查询-SAP接口服务
 */
@Service
public class SapCustReconService {
    private static final Logger logger = LoggerFactory.getLogger(SapCustReconService.class);
    @Resource
    private ClientCore clientCore;

    /**
     * 调用SAP查询客商对账数据
     */
    public SapCustReconCtrRet callSap(ClientMdmReqCommon req) {
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDSD090);
        SapCustReconCtrRet ret;
        try {
            ret = clientCore.inf.callSapXml(clientDestination, req, SapCustReconCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP客商对账查询接口错误:", e);
            return null;
        }
        if (ObjectUtils.isEmpty(ret)) {
            return null;
        }
        return ret;
    }
}

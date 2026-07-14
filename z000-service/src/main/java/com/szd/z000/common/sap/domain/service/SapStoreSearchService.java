package com.szd.z000.common.sap.domain.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.domain.common.ClientMdmReqCommon;
import com.szd.z000.base.common.constant.SapConstant;
import com.szd.z000.common.sap.domain.storeSearch.SapStoreSearchCtrRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * 物料凭证抽取-SAP接口服务
 */
@Service
public class SapStoreSearchService {
    private static final Logger logger = LoggerFactory.getLogger(SapStoreSearchService.class);
    @Resource
    private ClientCore clientCore;

    /**
     * 调用SAP查询物料凭证数据
     */
    public SapStoreSearchCtrRet callSap(ClientMdmReqCommon req) {
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDMM022);
        SapStoreSearchCtrRet ret;
        try {
            ret = clientCore.inf.callSapXml(clientDestination, req, SapStoreSearchCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP物料凭证抽取接口错误:", e);
            return null;
        }
        if (ObjectUtils.isEmpty(ret)) {
            return null;
        }
        return ret;
    }
}

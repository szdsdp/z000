package com.szd.z000.common.sap.domain.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.domain.common.ClientMdmReqCommon;
import com.szd.z000.base.common.constant.SapConstant;
import com.szd.z000.common.sap.domain.saleDeliverySearch.SapSaleDeliverySearchCtrRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * 交货单抽取-SAP接口服务
 */
@Service
public class SapSaleDeliverySearchService {
    private static final Logger logger = LoggerFactory.getLogger(SapSaleDeliverySearchService.class);
    @Resource
    private ClientCore clientCore;

    /**
     * 调用SAP查询交货单数据
     */
    public SapSaleDeliverySearchCtrRet callSap(ClientMdmReqCommon req) {
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDSD011);
        SapSaleDeliverySearchCtrRet ret;
        try {
            ret = clientCore.inf.callSapXml(clientDestination, req, SapSaleDeliverySearchCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP交货单抽取接口错误:", e);
            return null;
        }
        if (ObjectUtils.isEmpty(ret)) {
            return null;
        }
        return ret;
    }
}

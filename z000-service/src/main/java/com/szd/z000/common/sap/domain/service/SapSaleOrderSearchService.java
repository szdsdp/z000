package com.szd.z000.common.sap.domain.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.domain.common.ClientMdmReqCommon;
import com.szd.z000.base.common.constant.SapConstant;
import com.szd.z000.common.sap.domain.saleOrderSearch.SapSaleOrderSearchCtrRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * 销售订单抽取-SAP接口服务
 */
@Service
public class SapSaleOrderSearchService {
    private static final Logger logger = LoggerFactory.getLogger(SapSaleOrderSearchService.class);
    @Resource
    private ClientCore clientCore;

    /**
     * 调用SAP查询销售订单数据
     */
    public SapSaleOrderSearchCtrRet callSap(ClientMdmReqCommon req) {
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDSD002);
        SapSaleOrderSearchCtrRet ret;
        try {
            ret = clientCore.inf.callSapXml(clientDestination, req, SapSaleOrderSearchCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP销售订单抽取接口错误:", e);
            return null;
        }
        if (ObjectUtils.isEmpty(ret)) {
            return null;
        }
        return ret;
    }
}

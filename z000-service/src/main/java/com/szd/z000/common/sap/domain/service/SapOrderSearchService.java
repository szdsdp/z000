package com.szd.z000.common.sap.domain.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.domain.common.ClientMdmReqCommon;
import com.szd.z000.base.common.constant.SapConstant;
import com.szd.z000.common.sap.domain.orderSearch.SapOrderSearchCtrRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * 采购订单抽取-SAP接口服务
 */
@Service
public class SapOrderSearchService {
    private static final Logger logger = LoggerFactory.getLogger(SapOrderSearchService.class);
    @Resource
    private ClientCore clientCore;

    /**
     * 调用SAP查询采购订单数据
     */
    public SapOrderSearchCtrRet callSap(ClientMdmReqCommon req) {
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDMM011);
        SapOrderSearchCtrRet ret;
        try {
            ret = clientCore.inf.callSapXml(clientDestination, req, SapOrderSearchCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP采购订单抽取接口错误:", e);
            return null;
        }
        if (ObjectUtils.isEmpty(ret)) {
            return null;
        }
        return ret;
    }
}

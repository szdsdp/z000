package com.szd.z000.common.sap.domain.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientResultMsgBox;
import com.szd.z000.base.common.constant.SapConstant;
import com.szd.z000.base.orm.domain.sd.saleDelivery.SaleDeliveryPost;
import com.szd.z000.base.orm.mapper.sd.saleDelivery.SaleDeliveryPostMapper;
import com.szd.z000.common.sap.domain.saleDelivery.SapSaleDeliveryPostCtr;
import com.szd.z000.common.sap.domain.saleDelivery.SapSaleDeliveryPostCtrRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * SAP销售交货单发货过账接口服务类
 */
@Service
public class SapSaleDeliveryPostService {
    private static final Logger logger = LoggerFactory.getLogger(SapSaleDeliveryPostService.class);
    @Resource
    private SaleDeliveryPostMapper saleDeliveryPostMapper;
    @Resource
    private ClientCore clientCore;

    /**
     * 发货过账
     */
    @Transactional(rollbackFor = Exception.class)
    public ClientResult postSaleDelivery(SapSaleDeliveryPostCtr sapSaleDeliveryPostCtr, SaleDeliveryPost postData) {
        ClientResult clientResult = new ClientResult();
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDSD020);
        SapSaleDeliveryPostCtrRet ret = null;
        try {
            ret = clientCore.inf.callSapXml(clientDestination, sapSaleDeliveryPostCtr, SapSaleDeliveryPostCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP发货过账接口错误:", e);
            clientResult.setStatus("E");
            clientResult.setMsg("发货过账接口错误:" + e.getMessage());
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP发货过账接口错误," + e.getMessage());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        }
        if (!ObjectUtils.isEmpty(ret)) {
            // 将前端传来的日期 + SAP返回值一起保存到数据库
            postData.setMblnr(ret.getMblnr());
            postData.setMjahr(ret.getMjahr());
            postData.setSapMsg(truncate(ret.getZmess()));
            postData.setSapMessId(clientDestination.getMessId());
            SaleDeliveryPost exist = saleDeliveryPostMapper.selectById(postData.getDeliveryId());
            if (exist == null) {
                saleDeliveryPostMapper.insert(postData);
            } else {
                saleDeliveryPostMapper.update(postData);
            }
            String retMessage;
            String retStatus;
            if ("S".equals(ret.getZstau())) {
                retStatus = "S";
                retMessage = "SAP发货过账成功," + ret.getMblnr();
            } else {
                retStatus = "E";
                retMessage = "SAP发货过账失败," + ret.getZmess();
            }
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox(retStatus, retMessage);
            clientResult.setStatus(retStatus);
            clientResult.setDocId(ret.getMblnr());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        } else {
            clientResult.setStatus("E");
            clientResult.setMsg("发货过账接口错误");
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP发货过账接口错误");
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        }
    }

    /**
     * 冲销发货过账
     */
    @Transactional(rollbackFor = Exception.class)
    public ClientResult reversePostSaleDelivery(SapSaleDeliveryPostCtr sapSaleDeliveryPostCtr, String deliveryId) {
        ClientResult clientResult = new ClientResult();
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDSD020);
        SapSaleDeliveryPostCtrRet ret = null;
        try {
            ret = clientCore.inf.callSapXml(clientDestination, sapSaleDeliveryPostCtr, SapSaleDeliveryPostCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP冲销发货过账接口错误:", e);
            clientResult.setStatus("E");
            clientResult.setMsg("冲销发货过账接口错误:" + e.getMessage());
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP冲销发货过账接口错误," + e.getMessage());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        }
        if (!ObjectUtils.isEmpty(ret)) {
            saleDeliveryPostMapper.updateReverseSapRet(deliveryId, ret.getMblnr(), ret.getMjahr(), truncate(ret.getZmess()), clientDestination.getMessId());
            String retMessage;
            String retStatus;
            if ("S".equals(ret.getZstau())) {
                retStatus = "S";
                retMessage = "SAP冲销发货过账成功," + ret.getMblnr();
            } else {
                retStatus = "E";
                retMessage = "SAP冲销发货过账失败," + ret.getZmess();
            }
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox(retStatus, retMessage);
            clientResult.setStatus(retStatus);
            clientResult.setDocId(ret.getMblnr());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        } else {
            clientResult.setStatus("E");
            clientResult.setMsg("冲销发货过账接口错误");
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP冲销发货过账接口错误");
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        }
    }

    private String truncate(String input) {
        if (input == null) {
            return null;
        }
        if (input.length() <= 200) {
            return input;
        }
        return input.substring(0, 200);
    }
}

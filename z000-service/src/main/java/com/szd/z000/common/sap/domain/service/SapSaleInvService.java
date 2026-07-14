package com.szd.z000.common.sap.domain.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientResultMsgBox;
import com.szd.z000.base.common.constant.SapConstant;
import com.szd.z000.base.orm.domain.sd.saleInv.SaleInvHeader;
import com.szd.z000.base.orm.mapper.sd.saleInv.SaleInvHeaderMapper;
import com.szd.z000.common.sap.domain.saleInv.SapSaleInvCtr;
import com.szd.z000.common.sap.domain.saleInv.SapSaleInvCtrRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * SAP销售开票接口服务类
 */
@Service
public class SapSaleInvService {
    private static final Logger logger = LoggerFactory.getLogger(SapSaleInvService.class);
    @Resource
    private SaleInvHeaderMapper saleInvHeaderMapper;
    @Resource
    private ClientCore clientCore;

    /**
     * 出具发票
     */
    @Transactional(rollbackFor = Exception.class)
    public ClientResult issueInvoice(SapSaleInvCtr sapCtr, SaleInvHeader header) {
        ClientResult clientResult = new ClientResult();
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDSD030);
        SapSaleInvCtrRet ret = null;
        try {
            ret = clientCore.inf.callSapXml(clientDestination, sapCtr, SapSaleInvCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP出具发票接口错误:", e);
            clientResult.setStatus("E");
            clientResult.setMsg("出具发票接口错误:" + e.getMessage());
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP出具发票接口错误," + e.getMessage());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        }
        if (!ObjectUtils.isEmpty(ret)) {
            String retMessage;
            String retStatus;
            if ("S".equals(ret.getZstau())) {
                // 将SAP返回值设置到header
                header.setBillDoc(ret.getVbeln());
                header.setSapMsg(truncate(ret.getZmess()));
                header.setSapMessId(clientDestination.getMessId());
                // 更新抬头SAP返回值
                saleInvHeaderMapper.updateSapRet(header.getInvId(), header.getBillDoc(),
                        header.getSapMsg(), header.getSapMessId());
                retStatus = "S";
                retMessage = "SAP出具发票成功," + ret.getVbeln();
            } else {
                retStatus = "E";
                retMessage = "SAP出具发票失败," + ret.getZmess();
            }
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox(retStatus, retMessage);
            clientResult.setStatus(retStatus);
            clientResult.setDocId(ret.getVbeln());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        } else {
            clientResult.setStatus("E");
            clientResult.setMsg("出具发票接口错误");
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP出具发票接口错误");
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        }
    }

    /**
     * 冲销出具发票
     */
    @Transactional(rollbackFor = Exception.class)
    public ClientResult cancelInvoice(SapSaleInvCtr sapCtr, String invId) {
        ClientResult clientResult = new ClientResult();
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDSD030);
        SapSaleInvCtrRet ret = null;
        try {
            ret = clientCore.inf.callSapXml(clientDestination, sapCtr, SapSaleInvCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP冲销出具发票接口错误:", e);
            clientResult.setStatus("E");
            clientResult.setMsg("冲销出具发票接口错误:" + e.getMessage());
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP冲销出具发票接口错误," + e.getMessage());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        }
        if (!ObjectUtils.isEmpty(ret)) {
            String retMessage;
            String retStatus;
            if ("S".equals(ret.getZstau())) {
                saleInvHeaderMapper.updateReverseSapRet(invId, ret.getVbeln(),
                        truncate(ret.getZmess()), clientDestination.getMessId());
                retStatus = "S";
                retMessage = "SAP冲销出具发票成功," + ret.getVbeln();
            } else {
                retStatus = "E";
                retMessage = "SAP冲销出具发票失败," + ret.getZmess();
            }
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox(retStatus, retMessage);
            clientResult.setStatus(retStatus);
            clientResult.setDocId(ret.getVbeln());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        } else {
            clientResult.setStatus("E");
            clientResult.setMsg("冲销出具发票接口错误");
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP冲销出具发票接口错误");
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

package com.szd.z000.common.sap.domain.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientResultMsgBox;
import com.szd.z000.base.common.constant.SapConstant;
import com.szd.z000.base.orm.mapper.sd.payRece.PayReceiptHeaderMapper;
import com.szd.z000.common.sap.domain.payRece.SapPayReceiptCtr;
import com.szd.z000.common.sap.domain.payRece.SapPayReceiptCtrRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * SAP回款认领单创建-接口服务
 */
@Service
public class SapPayReceiptService {
    private static final Logger logger = LoggerFactory.getLogger(SapPayReceiptService.class);

    @Resource
    private PayReceiptHeaderMapper payReceiptHeaderMapper;
    @Resource
    private ClientCore clientCore;

    /**
     * 创建SAP回款认领单
     *
     * @param sapPayReceiptCtr SAP请求数据
     * @param receiptId        回款认领单ID
     * @return 处理结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ClientResult createPayReceipt(SapPayReceiptCtr sapPayReceiptCtr, String receiptId) {
        ClientResult clientResult = new ClientResult();
        // 系统标识
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDSD040);
        // 调用SAP接口
        SapPayReceiptCtrRet ret = null;
        try {
            ret = clientCore.inf.callSapXml(clientDestination, sapPayReceiptCtr, SapPayReceiptCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP创建回款认领单接口错误:", e);
            clientResult.setStatus("E");
            clientResult.setMsg("创建回款认领单接口错误:" + e.getMessage());
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP创建回款认领单接口错误," + e.getMessage());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        }
        if (!ObjectUtils.isEmpty(ret)) {
            String retMessage;
            String retStatus;
            if ("S".equals(ret.getZstau())) {
                // 更新凭证号、财年、消息
                payReceiptHeaderMapper.updateSapVchInfo(receiptId, ret.getBelnr(), ret.getGjahr(),
                        clientDestination.getMessId(), truncate(ret.getZmess()));
                retStatus = "S";
                retMessage = "SAP创建回款认领单成功," + ret.getBelnr();
            } else {
                // 失败时只更新消息
                payReceiptHeaderMapper.updateSapMsg(receiptId, clientDestination.getMessId(), truncate(ret.getZmess()));
                retStatus = "E";
                retMessage = "SAP创建回款认领单失败," + ret.getZmess();
            }
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox(retStatus, retMessage);
            clientResult.setStatus(retStatus);
            clientResult.setDocId(ret.getBelnr());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        } else {
            clientResult.setStatus("E");
            clientResult.setMsg("创建回款认领单接口错误");
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP创建回款认领单接口错误");
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        }
    }

    /**
     * 冲销SAP回款认领单
     *
     * @param sapPayReceiptCtr SAP请求数据（ZTYPE=R）
     * @param receiptId        回款认领单ID
     * @return 处理结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ClientResult reversePayReceipt(SapPayReceiptCtr sapPayReceiptCtr, String receiptId) {
        ClientResult clientResult = new ClientResult();
        // 系统标识
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDSD040);
        // 调用SAP接口
        SapPayReceiptCtrRet ret = null;
        try {
            ret = clientCore.inf.callSapXml(clientDestination, sapPayReceiptCtr, SapPayReceiptCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP冲销回款认领单接口错误:", e);
            clientResult.setStatus("E");
            clientResult.setMsg("冲销回款认领单接口错误:" + e.getMessage());
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP冲销回款认领单接口错误," + e.getMessage());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        }
        if (!ObjectUtils.isEmpty(ret)) {
            String retMessage;
            String retStatus;
            if ("S".equals(ret.getZstau())) {
                // 更新冲销凭证号、冲销财年、消息
                payReceiptHeaderMapper.updateSapReverseVchInfo(receiptId, ret.getBelnr(), ret.getGjahr(),
                        clientDestination.getMessId(), truncate(ret.getZmess()));
                retStatus = "S";
                retMessage = "SAP冲销回款认领单成功," + ret.getBelnr();
            } else {
                // 失败时只更新消息
                payReceiptHeaderMapper.updateSapMsg(receiptId, clientDestination.getMessId(), truncate(ret.getZmess()));
                retStatus = "E";
                retMessage = "SAP冲销回款认领单失败," + ret.getZmess();
            }
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox(retStatus, retMessage);
            clientResult.setStatus(retStatus);
            clientResult.setDocId(ret.getBelnr());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        } else {
            clientResult.setStatus("E");
            clientResult.setMsg("冲销回款认领单接口错误");
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP冲销回款认领单接口错误");
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        }
    }

    /**
     * 截断字符串到200字符以内
     */
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

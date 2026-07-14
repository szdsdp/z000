package com.szd.z000.common.sap.domain.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientResultMsgBox;
import com.szd.z000.base.common.constant.SapConstant;
import com.szd.z000.base.orm.mapper.mm.purStore.PurStoreHeaderMapper;
import com.szd.z000.common.sap.domain.purStore.SapPurStoreCtrRet;
import com.szd.z000.common.sap.domain.purStore.SapPurStoreReverseCtr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * SAP物料凭证冲销接口服务类
 */
@Service
public class SapPurStoreReverseService {
    private static final Logger logger = LoggerFactory.getLogger(SapPurStoreReverseService.class);
    @Resource
    private PurStoreHeaderMapper purStoreHeaderMapper;
    @Resource
    private ClientCore clientCore;

    /***
     * 冲销物料凭证
     */
    @Transactional(rollbackFor = Exception.class)
    public ClientResult reversePurStore(SapPurStoreReverseCtr sapPurStoreReverseCtr, String storeId) {
        ClientResult clientResult = new ClientResult();
        // 系统标识
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDMM021);
        // 调用接口
        SapPurStoreCtrRet ret = null;
        try {
            ret = clientCore.inf.callSapXml(clientDestination, sapPurStoreReverseCtr, SapPurStoreCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP冲销物料凭证接口错误:", e);
            clientResult.setStatus("E");
            clientResult.setMsg("冲销物料凭证接口错误:" + e.getMessage());
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP冲销物料凭证接口错误," + e.getMessage());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        }
        if (!ObjectUtils.isEmpty(ret)) {
            purStoreHeaderMapper.updateReverseSapRet(storeId, ret.getMblnr(), ret.getMjahr(), truncate(ret.getZmess()), clientDestination.getMessId());
            String retMessage;
            String retStatus;
            if ("S".equals(ret.getZstau())) {
                retStatus = "S";
                retMessage = "SAP冲销物料凭证成功," + ret.getMblnr();
            } else {
                retStatus = "E";
                retMessage = "SAP冲销物料凭证失败," + ret.getZmess();
            }
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox(retStatus, retMessage);
            clientResult.setStatus(retStatus);
            clientResult.setDocId(ret.getMblnr());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        } else {
            clientResult.setStatus("E");
            clientResult.setMsg("冲销物料凭证接口错误");
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP冲销物料凭证接口错误");
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        }
    }

    public String truncate(String input) {
        if (input == null) {
            return null;
        }
        if (input.length() <= 200) {
            return input;
        }
        return input.substring(0, 200);
    }
}

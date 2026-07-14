package com.szd.z000.common.sap.domain.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientResultMsgBox;
import com.szd.z000.base.common.constant.SapConstant;
import com.szd.z000.base.orm.mapper.mm.purStore.PurStoreHeaderMapper;
import com.szd.z000.common.sap.domain.purStore.SapPurStoreCtr;
import com.szd.z000.common.sap.domain.purStore.SapPurStoreCtrRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * SAP物料凭证接口服务类
 */
@Service
public class SapPurStoreCreateService {
    private static final Logger logger = LoggerFactory.getLogger(SapPurStoreCreateService.class);
    @Resource
    private PurStoreHeaderMapper purStoreHeaderMapper;
    @Resource
    private ClientCore clientCore;

    /***
     * 创建物料凭证
     */
    @Transactional(rollbackFor = Exception.class)
    public ClientResult createPurStore(SapPurStoreCtr sapPurStoreCtr, String storeId) {
        ClientResult clientResult = new ClientResult();
        // 系统标识
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDMM020);
        // 调用接口
        SapPurStoreCtrRet ret = null;
        try {
            ret = clientCore.inf.callSapXml(clientDestination, sapPurStoreCtr, SapPurStoreCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP创建物料凭证接口错误:", e);
            clientResult.setStatus("E");
            clientResult.setMsg("创建物料凭证接口错误:" + e.getMessage());
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP创建物料凭证接口错误," + e.getMessage());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        }
        if (!ObjectUtils.isEmpty(ret)) {
            String retMessage;
            String retStatus;
            if ("S".equals(ret.getZstau())) {
                purStoreHeaderMapper.updateSapRet(storeId, ret.getMblnr(), ret.getMjahr(), truncate(ret.getZmess()), clientDestination.getMessId());
                retStatus = "S";
                retMessage = "SAP创建物料凭证成功," + ret.getMblnr();
            } else {
                retStatus = "E";
                retMessage = "SAP创建物料凭证失败," + ret.getZmess();
            }
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox(retStatus, retMessage);
            clientResult.setStatus(retStatus);
            clientResult.setDocId(ret.getMblnr());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        } else {
            clientResult.setStatus("E");
            clientResult.setMsg("创建物料凭证接口错误");
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP创建物料凭证接口错误");
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

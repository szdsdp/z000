package com.szd.z000.common.sap.domain.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientResultMsgBox;
import com.szd.z000.base.common.constant.SapConstant;
import com.szd.z000.base.orm.mapper.mm.purContract.PurContractHeaderMapper;
import com.szd.z000.common.sap.domain.purContract.SapPurContractCtr;
import com.szd.z000.common.sap.domain.purContract.SapPurContractCtrRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * SAP采购合同接口服务类
 */
@Service
public class SapPurContractCreateService {
    private static final Logger logger = LoggerFactory.getLogger(SapPurContractCreateService.class);
    @Resource
    private PurContractHeaderMapper purContractHeaderMapper;
    @Resource
    private ClientCore clientCore;

    /***
     * 创建采购合同
     */
    @Transactional(rollbackFor = Exception.class)
    public ClientResult createPurContract(SapPurContractCtr sapPurContractCtr, String contractId) {
        ClientResult clientResult = new ClientResult();
        // 系统标识
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDMM003);
        // 调用接口
        SapPurContractCtrRet ret = null;
        try {
            ret = clientCore.inf.callSapXml(clientDestination, sapPurContractCtr, SapPurContractCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP创建采购合同接口错误:", e);
            clientResult.setStatus("E");
            clientResult.setMsg("创建采购合同接口错误:" + e.getMessage());
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP创建采购合同接口错误," + e.getMessage());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        }
        if (!ObjectUtils.isEmpty(ret)) {
            String retMessage;
            String retStatus;
            if ("S".equals(ret.getZstau())) {
                purContractHeaderMapper.updatePurCont(contractId, ret.getEbeln(), truncate(ret.getZmess()), clientDestination.getMessId());
                retStatus = "S";
                retMessage = "SAP创建采购合同成功," + ret.getEbeln();
            } else {
                retStatus = "E";
                retMessage = "SAP创建采购合同失败," + ret.getZmess();
            }
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox(retStatus, retMessage);
            clientResult.setStatus(retStatus);
            clientResult.setDocId(ret.getEbeln());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        } else {
            clientResult.setStatus("E");
            clientResult.setMsg("创建采购合同接口错误");
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP创建采购合同接口错误");
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

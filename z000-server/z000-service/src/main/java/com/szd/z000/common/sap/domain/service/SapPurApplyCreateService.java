package com.szd.z000.common.sap.domain.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientResultMsgBox;
import com.szd.z000.base.common.constant.SapConstant;
import com.szd.z000.base.orm.mapper.mm.purApply.PurApplyHeaderMapper;
import com.szd.z000.common.sap.domain.purApply.SapPurApplyCtr;
import com.szd.z000.common.sap.domain.purApply.SapPurApplyCtrRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * SAP采购申请接口服务类
 */
@Service
public class SapPurApplyCreateService {
    private static final Logger logger = LoggerFactory.getLogger(SapPurApplyCreateService.class);
    @Resource
    private PurApplyHeaderMapper purApplyHeaderMapper;
    @Resource
    private ClientCore clientCore;

    /***
     * 创建采购申请
     */
    @Transactional(rollbackFor = Exception.class)
    public ClientResult createPurApply(SapPurApplyCtr sapPurApplyCtr, String applyId) {
        ClientResult clientResult = new ClientResult();
        // 系统标识
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDMM002);
        // 调用接口
        SapPurApplyCtrRet ret = null;
        try {
            ret = clientCore.inf.callSapXml(clientDestination, sapPurApplyCtr, SapPurApplyCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP创建采购申请接口错误:", e);
            clientResult.setStatus("E");
            clientResult.setMsg("创建采购申请接口错误:" + e.getMessage());
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP创建采购申请接口错误," + e.getMessage());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        }
        if (!ObjectUtils.isEmpty(ret)) {
            String retMessage;
            String retStatus;
            if ("S".equals(ret.getZstau())) {
                purApplyHeaderMapper.updateApplyNo(applyId, ret.getBanfn(), truncate(ret.getZmess()), clientDestination.getMessId());
                retStatus = "S";
                retMessage = "SAP创建采购申请成功," + ret.getBanfn();
            } else {
                retStatus = "E";
                retMessage = "SAP创建采购申请失败," + ret.getZmess();
            }
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox(retStatus, retMessage);
            clientResult.setStatus(retStatus);
            clientResult.setDocId(ret.getBanfn());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        } else {
            clientResult.setStatus("E");
            clientResult.setMsg("创建采购申请接口错误");
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP创建采购申请接口错误");
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

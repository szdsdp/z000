package com.szd.z000.common.sap.domain.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientResultMsgBox;
import com.szd.z000.base.common.constant.SapConstant;
import com.szd.z000.base.orm.mapper.mm.invCheck.InvCheckHeaderMapper;
import com.szd.z000.common.sap.domain.invCheck.SapInvCheckCtr;
import com.szd.z000.common.sap.domain.invCheck.SapInvCheckCtrRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * SAP发票校验接口服务类（正向+冲销）
 */
@Service
public class SapInvCheckCreateService {
    private static final Logger logger = LoggerFactory.getLogger(SapInvCheckCreateService.class);
    @Resource
    private InvCheckHeaderMapper invCheckHeaderMapper;
    @Resource
    private ClientCore clientCore;

    /**
     * 调用SAP发票校验接口
     * @param sapInvCheckCtr 请求数据（正向IF_RFLAG为空，冲销时IF_RFLAG为X）
     * @param invId 单据ID
     * @param isReverse true=冲销，false=正向
     */
    @Transactional(rollbackFor = Exception.class)
    public ClientResult callInvCheck(SapInvCheckCtr sapInvCheckCtr, String invId, boolean isReverse) {
        ClientResult clientResult = new ClientResult();
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDMM030);
        SapInvCheckCtrRet ret = null;
        String action = isReverse ? "冲销" : "";
        try {
            ret = clientCore.inf.callSapXml(clientDestination, sapInvCheckCtr, SapInvCheckCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP" + action + "发票校验接口错误:", e);
            clientResult.setStatus("E");
            clientResult.setMsg(action + "发票校验接口错误:" + e.getMessage());
            ClientResultMsgBox msgBox = new ClientResultMsgBox("E", "SAP" + action + "发票校验接口错误," + e.getMessage());
            clientResult.getRetMsgBox().add(msgBox);
            return clientResult;
        }
        if (!ObjectUtils.isEmpty(ret)) {
            String retMessage;
            String retStatus;
            if ("S".equals(ret.getZstau())) {
                if (isReverse) {
                    // 冲销：保存到冲销字段
                    invCheckHeaderMapper.updateReverseSapRet(invId, ret.getReBelnrCx(), ret.getReGjahrCx(),
                            ret.getBelnrQz(), ret.getGjahrQz(), truncate(ret.getZmess()), clientDestination.getMessId());
                } else {
                    // 正向：保存到正向字段
                    invCheckHeaderMapper.updateSapRet(invId, ret.getReBelnr(), ret.getReGjahr(), ret.getBukrs(),
                            ret.getBelnr(), ret.getGjahr(), truncate(ret.getZmess()), clientDestination.getMessId());
                }
                retStatus = "S";
                retMessage = "SAP" + action + "发票校验成功," + (isReverse ? ret.getReBelnrCx() : ret.getReBelnr());
            } else {
                retStatus = "E";
                retMessage = "SAP" + action + "发票校验失败," + ret.getZmess();
            }
            ClientResultMsgBox msgBox = new ClientResultMsgBox(retStatus, retMessage);
            clientResult.setStatus(retStatus);
            clientResult.setDocId(isReverse ? ret.getReBelnrCx() : ret.getReBelnr());
            clientResult.getRetMsgBox().add(msgBox);
            return clientResult;
        } else {
            clientResult.setStatus("E");
            clientResult.setMsg(action + "发票校验接口错误");
            ClientResultMsgBox msgBox = new ClientResultMsgBox("E", "SAP" + action + "发票校验接口错误");
            clientResult.getRetMsgBox().add(msgBox);
            return clientResult;
        }
    }

    public String truncate(String input) {
        if (input == null) return null;
        if (input.length() <= 200) return input;
        return input.substring(0, 200);
    }
}

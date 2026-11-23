package com.szd.z000.common.sap.domain.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientResultMsgBox;
import com.szd.z000.base.common.constant.SapConstant;
import com.szd.z000.base.orm.mapper.mm.check.checkPlan.CheckPlanHeaderMapper;
import com.szd.z000.common.sap.domain.check.checkPlan.SapCheckPlanCtr;
import com.szd.z000.common.sap.domain.check.checkPlan.SapCheckPlanCtrRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * SAP库存盘点创建接口服务类
 */
@Service
public class SapCheckPlanCreateService {
    private static final Logger logger = LoggerFactory.getLogger(SapCheckPlanCreateService.class);
    @Resource
    private CheckPlanHeaderMapper checkPlanHeaderMapper;
    @Resource
    private ClientCore clientCore;

    /***
     * 创建库存盘点
     */
    @Transactional(rollbackFor = Exception.class)
    public ClientResult createCheckPlan(SapCheckPlanCtr sapCheckPlanCtr, String planId) {
        ClientResult clientResult = new ClientResult();
        // 系统标识（复用物料凭证的SAP业务类型）
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDMM020);
        // 调用接口
        SapCheckPlanCtrRet ret = null;
        try {
            ret = clientCore.inf.callSapXml(clientDestination, sapCheckPlanCtr, SapCheckPlanCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP创建库存盘点接口错误:", e);
            clientResult.setStatus("E");
            clientResult.setMsg("创建库存盘点接口错误:" + e.getMessage());
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP创建库存盘点接口错误," + e.getMessage());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        }
        if (!ObjectUtils.isEmpty(ret)) {
            String retMessage;
            String retStatus;
            if ("S".equals(ret.getZstau())) {
                checkPlanHeaderMapper.updateSapRet(planId, ret.getMblnr(), ret.getMjahr(), truncate(ret.getZmess()), clientDestination.getMessId());
                retStatus = "S";
                retMessage = "SAP创建库存盘点成功," + ret.getMblnr();
            } else {
                retStatus = "E";
                retMessage = "SAP创建库存盘点失败," + ret.getZmess();
            }
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox(retStatus, retMessage);
            clientResult.setStatus(retStatus);
            clientResult.setDocId(ret.getMblnr());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        } else {
            clientResult.setStatus("E");
            clientResult.setMsg("创建库存盘点接口错误");
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP创建库存盘点接口错误");
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

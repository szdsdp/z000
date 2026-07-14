package com.szd.z000.common.sap.domain.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientResultMsgBox;
import com.szd.z000.base.common.constant.SapConstant;
import com.szd.z000.base.orm.mapper.mdm.mtlcj.MtlcjHeaderMapper;
import com.szd.z000.common.sap.domain.mtlcj.SapMtlcjCtr;
import com.szd.z000.common.sap.domain.mtlcj.SapMtlcjCtrRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * SAP物料主数据接口服务类
 */
@Service
public class SapMtlcjService {
    private static final Logger logger = LoggerFactory.getLogger(SapMtlcjService.class);
    @Resource
    private MtlcjHeaderMapper mtlcjHeaderMapper;
    @Resource
    private ClientCore clientCore;

    /***
     * 创建/修改物料主数据
     */
    @Transactional(rollbackFor = Exception.class)
    public ClientResult createMtl(SapMtlcjCtr sapCtr, String applyId) {
        ClientResult clientResult = new ClientResult();
        // 系统标识
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDMDM019);
        // 调用接口
        SapMtlcjCtrRet ret = null;
        try {
            ret = clientCore.inf.callSapXml(clientDestination, sapCtr, SapMtlcjCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP物料主数据创建/修改接口错误:", e);
            clientResult.setStatus("E");
            clientResult.setMsg("物料主数据创建/修改接口错误:" + e.getMessage());
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP物料主数据创建/修改接口错误," + e.getMessage());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        }
        if (!ObjectUtils.isEmpty(ret)) {
            String retMessage;
            String retStatus;
            if ("S".equals(ret.getZstau())) {
                // SAP成功时更新物料编码
                mtlcjHeaderMapper.updateMtlCode(applyId, ret.getMatnr(), truncate(ret.getZmess()), clientDestination.getMessId());
                retStatus = "S";
                retMessage = "SAP创建/修改物料主数据成功," + ret.getMatnr();
            } else {
                retStatus = "E";
                retMessage = "SAP创建/修改物料主数据失败," + ret.getZmess();
            }
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox(retStatus, retMessage);
            clientResult.setStatus(retStatus);
            clientResult.setDocId(ret.getMatnr());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        } else {
            clientResult.setStatus("E");
            clientResult.setMsg("物料主数据创建/修改接口错误");
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP物料主数据创建/修改接口错误");
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

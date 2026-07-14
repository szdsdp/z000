package com.szd.z000.common.sap.domain.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientResultMsgBox;
import com.szd.z000.base.common.constant.SapConstant;
import com.szd.z000.base.orm.mapper.fico.vch.VchHeaderMapper;
import com.szd.z000.common.sap.domain.vch.SapVchCtr;
import com.szd.z000.common.sap.domain.vch.SapVchCtrRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * SAP会计凭证生成-接口服务
 */
@Service
public class SapVchCreateService {
    private static final Logger logger = LoggerFactory.getLogger(SapVchCreateService.class);

    @Resource
    private VchHeaderMapper vchHeaderMapper;
    @Resource
    private ClientCore clientCore;

    /***
     * 创建会计凭证
     */
    @Transactional(rollbackFor = Exception.class)
    public ClientResult createVch(SapVchCtr sapVchCtr, String costId) {
        ClientResult clientResult = new ClientResult();
        // 系统标识
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDFI016);
        // 调用接口
        SapVchCtrRet ret = null;
        try {
            ret = clientCore.inf.callSapXml(clientDestination, sapVchCtr, SapVchCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP创建会计凭证接口错误:", e);
            clientResult.setStatus("E");
            clientResult.setMsg("创建会计凭证接口错误:" + e.getMessage());
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP创建会计凭证接口错误," + e.getMessage());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        }
        if (!ObjectUtils.isEmpty(ret)) {
            String retMessage;
            String retStatus;
            if ("S".equals(ret.getZstau())) {
                vchHeaderMapper.updateSapInfo(costId, ret.getBelnr(), ret.getGjahr(), ret.getZmess(), clientDestination.getMessId());
                retStatus = "S";
                retMessage = "SAP创建会计凭证成功," + ret.getBelnr();
            } else {
                retStatus = "E";
                retMessage = "SAP创建会计凭证失败," + ret.getZmess();
            }
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox(retStatus, retMessage);
            clientResult.setStatus(retStatus);
            clientResult.setDocId(ret.getBelnr());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        } else {
            clientResult.setStatus("E");
            clientResult.setMsg("创建会计凭证接口错误");
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP创建会计凭证接口错误");
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        }
    }
}

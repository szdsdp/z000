package com.szd.z000.common.sap.domain.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientResultMsgBox;
import com.szd.core.client.util.ClientUtilString;
import com.szd.z000.base.common.constant.SapConstant;
import com.szd.z000.base.orm.mapper.mdm.cust.MdmCustHeaderMapper;
import com.szd.z000.common.sap.domain.cust.SapCustCheckCtr;
import com.szd.z000.common.sap.domain.cust.SapCustCtr;
import com.szd.z000.common.sap.domain.cust.SapCustCtrRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * SAP接口服务类
 */
@Service
public class SapCustCreateService {
    private static final Logger logger = LoggerFactory.getLogger(SapCustCreateService.class);
    @Resource
    private MdmCustHeaderMapper mdmCustHeaderMapper;
    @Resource
    private ClientCore clientCore;

    /***
     *  创建客商
     */
    @Transactional(rollbackFor = Exception.class)
    public ClientResult createCust(SapCustCtr sapCustCtr, String applyId) {
        ClientResult clientResult = new ClientResult();
        // 系统标识
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDMDM017);
        // 调用接口
        SapCustCtrRet ret = null;
        try {
            ret = clientCore.inf.callSapXml(clientDestination, sapCustCtr, SapCustCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP创建客商接口错误:", e);
            clientResult.setStatus("E");
            clientResult.setMsg("创建客商接口错误:" + e.getMessage());
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP创建客商接口错误," + e.getMessage());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        }
        if (!ObjectUtils.isEmpty(ret)) {
            String retMessage;
            String retStatus;
            if ("S".equals(ret.getZstau())) {
                mdmCustHeaderMapper.updateCustId(applyId, ret.getCustId(), truncate(ret.getZmess()), clientDestination.getMessId());
                retStatus = "S";
                retMessage = "SAP创建客商成功," + ret.getCustId();
            } else {
                retStatus = "E";
                retMessage = "SAP创建客商失败," + ret.getZmess();
            }
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox(retStatus, retMessage);
            clientResult.setStatus(retStatus);
            clientResult.setDocId(ret.getCustId());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        } else {
            clientResult.setStatus("E");
            clientResult.setMsg("创建客商接口错误");
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP创建客商接口错误");
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        }
    }

    /***
     *  检查客商重复
     */
    @Transactional(rollbackFor = Exception.class)
    public ClientResult checkCust(SapCustCheckCtr sapCustCheckCtr) {
        ClientResult clientResult = new ClientResult();
        // 系统标识
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDMDM018);
        // 调用接口
        SapCustCtrRet ret = null;
        try {
            ret = clientCore.inf.callSapXml(clientDestination, sapCustCheckCtr, SapCustCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP检查客商接口错误:", e);
            clientResult.setStatus("E");
            clientResult.setMsg("检查客商接口错误:" + e.getMessage());
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP检查客商接口错误," + e.getMessage());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        }
        if (!ObjectUtils.isEmpty(ret)) {
            String retMessage;
            String retStatus;
            if ("S".equals(ret.getZstau())) {
                if (ClientUtilString.isNotBlank(ret.getCustId())) {
                    retStatus = "W";
                    retMessage = "SAP客商" + ret.getCustId() + "已存在";
                } else {
                    retStatus = "S";
                    retMessage = "SAP客商验重通过";
                }
            } else {
                retStatus = "E";
                retMessage = "SAP检查客商失败," + ret.getZmess();
            }
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox(retStatus, retMessage);
            clientResult.setStatus(retStatus);
            clientResult.setDocId(ret.getCustId());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        } else {
            clientResult.setStatus("E");
            clientResult.setMsg("检查客商接口错误");
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("E", "SAP检查客商接口错误");
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

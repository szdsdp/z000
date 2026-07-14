package com.szd.z000.service.common.utils;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.inv.ClientInvGetList;
import com.szd.core.client.domain.inv.ClientInvGetRet;
import com.szd.core.client.domain.wf.ClientWfParamBuss;
import com.szd.z000.base.common.util.CommonService;
import com.szd.z000.service.common.service.SapBussBaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * SEXP工具类
 */
@Service
public class Z000Util {
    @Resource
    private Z000UtilBussBaseService bussBaseService;
    @Resource
    private ClientCore clientCore;

    public static Z000Util z000Util;
    @Resource
    private CommonService common;
    @Resource
    private SapBussBaseService bussBase;

    @PostConstruct
    public void init() {
        z000Util = this;
        z000Util.clientCore = this.clientCore;
        z000Util.bussBaseService = this.bussBaseService;
    }



    /**
     * 获取业务单号
     */
    public static String getBussId() {
        return z000Util.clientCore.tool.getNumSeq();
    }


    /**
     * 查询报账单发票汇总金额
     */
    public static BigDecimal getInvAmtSum(String bussId) {
        BigDecimal amtInv = BigDecimal.ZERO;
        if (StringUtils.isBlank(bussId)) {
            return amtInv;
        }
        ClientInvGetList param = new ClientInvGetList();
        param.setBussId(bussId);
        List<ClientInvGetList> paramList = new ArrayList<>();
        paramList.add(param);
        List<ClientInvGetRet> invGetRetList = z000Util.clientCore.inv.getInvList(paramList);
        for (ClientInvGetRet inv : invGetRetList) {
            if (inv.getHeader().getInvAmt() != null) {
                amtInv = amtInv.add(inv.getHeader().getInvAmt());
            }
        }
        return amtInv;
    }


    /**
     * 获取业务状态
     */
    public static String getBussStatus(String status, String rejectStatus) {
        return z000Util.common.getBussStatus(status, rejectStatus);
    }

    /**
     * 保存业务主体
     */
    public static void saveBussBase(Object header, String bussId, String cmpy, String cstc) {
        z000Util.bussBase.saveBussBase(header, bussId, cmpy, cstc);
    }

    /**
     * 保存业务主体
     */
    public static void saveBussBase(Object header) {
        z000Util.bussBase.saveBussBase(header);
    }

    /**
     * 保存业务主体
     */
    public static void saveBussBase(Object header, String bussId, String cmpy, String cstc, String userId) {
        z000Util.bussBase.saveBussBase(header, bussId, cmpy, cstc, userId);
    }

    /**
     * 设置业务参数
     */
    public static ClientWfParamBuss setWfParamBuss(ClientWfParamBuss wfParamBuss, Object header, String bussId) {
        return z000Util.bussBase.setWfParamBuss(wfParamBuss, header, bussId);
    }

    /**
     * 主数据校验结果
     */
    public static void checkResult(ClientResult res) {
        z000Util.common.checkResult(res);
    }

    /**
     * 设置流程字段
     */
    public static ClientWfParamBuss setWfParamBuss(Object header, String bussId) {
        return z000Util.bussBase.setWfParamBuss(header, bussId);
    }
}

package com.szd.z000.common.sap.domain.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientResultMsgBox;
import com.szd.z000.base.common.constant.SapConstant;
import com.szd.z000.base.orm.mapper.pp.orderReport.OrderReportHeaderMapper;
import com.szd.z000.common.sap.domain.orderReport.SapOrderReportCtr;
import com.szd.z000.common.sap.domain.orderReport.SapOrderReportCtrRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * SAP生产订单报工接口服务类
 */
@Service
public class SapOrderReportCreateService {
    private static final Logger logger = LoggerFactory.getLogger(SapOrderReportCreateService.class);
    @Resource
    private OrderReportHeaderMapper orderReportHeaderMapper;
    @Resource
    private ClientCore clientCore;

    /***
     * 创建/冲销生产订单报工
     * @param isReverse true-冲销 false-正向报工
     */
    @Transactional(rollbackFor = Exception.class)
    public ClientResult createOrderReport(SapOrderReportCtr sapOrderReportCtr, String reportId, boolean isReverse) {
        ClientResult clientResult = new ClientResult();
        ClientDestination clientDestination = new ClientDestination();
        clientDestination.setBussTp(SapConstant.SZDPP003);
        SapOrderReportCtrRet ret = null;
        try {
            ret = clientCore.inf.callSapXml(clientDestination, sapOrderReportCtr, SapOrderReportCtrRet.class);
        } catch (Exception e) {
            logger.error("调用SAP生产订单报工接口错误:", e);
            clientResult.setStatus("E");
            clientResult.setMsg("生产订单报工接口错误:" + e.getMessage());
            clientResult.getRetMsgBox().add(new ClientResultMsgBox("E", "SAP生产订单报工接口错误," + e.getMessage()));
            return clientResult;
        }
        if (!ObjectUtils.isEmpty(ret)) {
            String retStatus, retMessage;
            if ("S".equals(ret.getZstau())) {
                retStatus = "S";
                retMessage = (isReverse ? "SAP冲销报工成功" : "SAP创建报工成功") + "," + ret.getConfno();
                if (isReverse) {
                    orderReportHeaderMapper.updateReverseSapRet(reportId, ret.getConfcnt(), truncate(ret.getZmess()), clientDestination.getMessId());
                } else {
                    orderReportHeaderMapper.updateSapRet(reportId, ret.getConfno(), ret.getConfcnt(), truncate(ret.getZmess()), clientDestination.getMessId());
                }
            } else {
                retStatus = "E";
                retMessage = (isReverse ? "SAP冲销报工失败" : "SAP创建报工失败") + "," + ret.getZmess();
            }
            ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox(retStatus, retMessage);
            clientResult.setStatus(retStatus);
            clientResult.setDocId(ret.getConfno());
            clientResult.getRetMsgBox().add(clientResultMsgBox);
            return clientResult;
        } else {
            clientResult.setStatus("E");
            clientResult.setMsg("生产订单报工接口错误");
            clientResult.getRetMsgBox().add(new ClientResultMsgBox("E", "SAP生产订单报工接口错误"));
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

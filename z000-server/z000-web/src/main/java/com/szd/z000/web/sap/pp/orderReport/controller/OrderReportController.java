package com.szd.z000.web.sap.pp.orderReport.controller;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.*;
import com.szd.core.client.domain.wf.ClientWfOper;
import com.szd.core.client.domain.wf.ClientWfReturn;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.service.ClientBaseController;
import com.szd.z000.base.orm.domain.pp.orderExtract.OrderExtractHeader;
import com.szd.z000.base.orm.domain.pp.orderExtract.OrderExtractProcess;
import com.szd.z000.base.orm.domain.pp.orderReport.OrderReportHeader;
import com.szd.z000.base.orm.domain.pp.orderReport.OrderReportItem;
import com.szd.z000.base.orm.mapper.pp.orderExtract.OrderExtractHeaderMapper;
import com.szd.z000.base.orm.mapper.pp.orderExtract.OrderExtractProcessMapper;
import com.szd.z000.web.sap.pp.orderReport.domain.OrderReportDataVO;
import com.szd.z000.web.sap.pp.orderReport.domain.OrderReportParamVO;
import com.szd.z000.web.sap.pp.orderReport.domain.OrderReportResultVO;
import com.szd.z000.web.sap.pp.orderReport.service.OrderReportSapService;
import com.szd.z000.web.sap.pp.orderReport.service.OrderReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 生产订单报工接口
 */
@Slf4j
@RestController
@RequestMapping("/z000/pp/orderReport")
public class OrderReportController extends ClientBaseController {
    @Resource
    private OrderReportService orderReportService;
    @Resource
    private OrderReportSapService orderReportSapService;
    @Resource
    private OrderExtractHeaderMapper orderExtractHeaderMapper;
    @Resource
    private OrderExtractProcessMapper orderExtractProcessMapper;
    @Resource
    private ClientCore clientCore;

    /**
     * 列表查询
     */
    @PostMapping("/list")
    public ClientPageTableRet list(@RequestBody ClientRpParam param) {
        startPage(param.getPageNum(), param.getPageSize());
        List<OrderReportHeader> list = orderReportService.selectList(param);
        getDescList(list);
        return getPageTable(list);
    }

    /**
     * 页面查询(包含业务数据、流程按钮、字段状态)
     */
    @GetMapping("/get")
    public ClientAjaxResult getData(String bussId, String userId, String option) {
        OrderReportDataVO data = orderReportService.selectData(bussId);
        getDesc(data);
        ClientWfOper wfOper = clientCore.wf.wfInit(bussId, userId, option);
        setWfOper(data, wfOper);
        OrderReportResultVO resultVO = new OrderReportResultVO();
        resultVO.setData(data);
        resultVO.setWfOper(wfOper);
        return ClientAjaxResult.success(resultVO);
    }

    private void getDesc(OrderReportDataVO data) {
        getDescHeader(data);
        getDescItemList(data);
    }

    private void getDescItemList(OrderReportDataVO data) {
        if (!CollectionUtils.isEmpty(data.getItemList())) {
            for (OrderReportItem item : data.getItemList()) {
                item.setOperNoName(clientCore.mdm.getDescA("Z000_PP_SCDDBG_OPERATION", item.getOperNo(), "1"));
            }
        }
    }

    /**
     * 数据检查
     */
    @PostMapping("/check")
    public ClientAjaxResult check(@RequestBody OrderReportParamVO paramVO) {
        try {
            ClientResult checkResult = orderReportService.check(paramVO);
            return ClientAjaxResult.success(checkResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 页面按钮操作(保存草稿，提交，同意，驳回...)
     */
    @PostMapping("/save")
    public ClientAjaxResult saveData(@RequestBody OrderReportParamVO paramVO) {
        try {
            ClientWfReturn clientWfReturn = orderReportService.saveData(paramVO);
            return ClientAjaxResult.success(clientWfReturn);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 同步SAP
     */
    @GetMapping("/syncSap/{reportId}")
    public ClientAjaxResult genSapDoc(@PathVariable("reportId") String reportId) {
        try {
            ClientResult clientResult = orderReportSapService.genSapOrderReport(reportId);
            if (CollectionUtils.isEmpty(clientResult.getRetMsgBox())) {
                ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("2", "", "S", "无需同步");
                clientResult.getRetMsgBox().add(clientResultMsgBox);
            }
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 校验扫描的订单号+工序编号是否合法
     */
    @GetMapping("/validateScan")
    public ClientAjaxResult validateScan(String orderNo, String operNo) {
        // 校验订单号
        OrderExtractHeader order = orderExtractHeaderMapper.selectById(orderNo);
        if (order == null) {
            return ClientAjaxResult.error("订单号" + orderNo + "不存在，请先抽取生产订单数据");
        }
        // 校验工序编号
        List<OrderExtractProcess> processList = orderExtractProcessMapper.selectByOrderNo(orderNo);
        OrderExtractProcess matched = null;
        if (!CollectionUtils.isEmpty(processList)) {
            for (OrderExtractProcess p : processList) {
                if (operNo.equals(p.getOperNo())) {
                    matched = p;
                    break;
                }
            }
        }
        if (matched == null) {
            return ClientAjaxResult.error("工序编号" + operNo + "在订单" + orderNo + "中不存在");
        }
        // 返回工序描述
        java.util.Map<String, String> result = new java.util.HashMap<>();
        result.put("operNo", operNo);
        result.put("operDesc", matched.getOperDesc() != null ? matched.getOperDesc() : "");
        return ClientAjaxResult.success(result);
    }

    /**
     * 从生产订单抽取工序创建报工单(草稿状态)
     */
    @PostMapping("/createFromOrderExtract")
    public ClientAjaxResult createFromOrderExtract(@RequestBody OrderReportParamVO paramVO) {
        try {
            String reportId = orderReportService.createFromOrderExtract(paramVO);
            ClientResult clientResult = new ClientResult();
            clientResult.setStatus("S");
            clientResult.setDocId(reportId);
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 冲销报工
     */
    @GetMapping("/reverse/{reportId}")
    public ClientAjaxResult reverse(@PathVariable("reportId") String reportId) {
        try {
            ClientResult clientResult = orderReportSapService.reverseSapOrderReport(reportId);
            if (CollectionUtils.isEmpty(clientResult.getRetMsgBox())) {
                ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("2", "", "S", "无需冲销");
                clientResult.getRetMsgBox().add(clientResultMsgBox);
            }
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    private void getDescList(List<OrderReportHeader> headerList) {
        if (!CollectionUtils.isEmpty(headerList)) {
            for (OrderReportHeader header : headerList) {
                header.setBussId(header.getReportId());
                header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "0"));
                header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "0"));
                header.setBstpName(clientCore.mdm.getDescA("CORE_BSTP", header.getBstp(), "0"));
                header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "0"));
                header.setCreateByName(clientCore.mdm.getDescA("CORE_USER", header.getCreateBy(), "0"));
                header.setUpdateByName(clientCore.mdm.getDescA("CORE_USER", header.getUpdateBy(), "0"));
            }
        }
    }

    private void getDescHeader(OrderReportDataVO data) {
        if (data.getHeader() != null) {
            OrderReportHeader header = data.getHeader();
            header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "1"));
            header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "1"));
            header.setBstpName(clientCore.mdm.getDescA("CORE_BSTP", header.getBstp(), "1"));
            header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "1"));
        }
    }

    private static void setWfOper(OrderReportDataVO data, ClientWfOper wfOper) {
        String bussStatus = data.getHeader().getBussStatus();
        if ("D".equals(bussStatus)) {
            wfOper.getWfBtn().setBtn_delete("V");
            wfOper.getWfBtn().setBtn_revoke("V");
        }
    }
}

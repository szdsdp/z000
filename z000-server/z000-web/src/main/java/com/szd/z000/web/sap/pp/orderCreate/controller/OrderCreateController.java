package com.szd.z000.web.sap.pp.orderCreate.controller;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.*;
import com.szd.core.client.domain.wf.ClientWfOper;
import com.szd.core.client.domain.wf.ClientWfReturn;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.service.ClientBaseController;
import com.szd.z000.base.orm.domain.pp.orderCreate.OrderCreateHeader;
import com.szd.z000.web.sap.pp.orderCreate.domain.OrderCreateDataVO;
import com.szd.z000.web.sap.pp.orderCreate.domain.OrderCreateParamVO;
import com.szd.z000.web.sap.pp.orderCreate.domain.OrderCreateResultVO;
import com.szd.z000.web.sap.pp.orderCreate.service.OrderCreateSapService;
import com.szd.z000.web.sap.pp.orderCreate.service.OrderCreateService;
import com.szd.z000.web.sap.mdm.mtl.service.MtlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 生产订单创建接口
 */
@Slf4j
@RestController
@RequestMapping("/z000/pp/orderCreate")
public class OrderCreateController extends ClientBaseController {
    @Resource
    private OrderCreateService orderCreateService;
    @Resource
    private OrderCreateSapService orderCreateSapService;
    @Resource
    private MtlService mtlService;
    @Resource
    private ClientCore clientCore;

    /**
     * 列表查询
     */
    @PostMapping("/list")
    public ClientPageTableRet list(@RequestBody ClientRpParam param) {
        startPage(param.getPageNum(), param.getPageSize());
        List<OrderCreateHeader> list = orderCreateService.selectList(param);
        getDescList(list);
        return getPageTable(list);
    }

    /**
     * 页面查询(包含业务数据、流程按钮、字段状态)
     */
    @GetMapping("/get")
    public ClientAjaxResult getData(String bussId, String userId, String option) {
        OrderCreateDataVO data = orderCreateService.selectData(bussId);
        getDesc(data);
        ClientWfOper wfOper = clientCore.wf.wfInit(bussId, userId, option);
        setWfOper(data, wfOper);
        OrderCreateResultVO resultVO = new OrderCreateResultVO();
        resultVO.setData(data);
        resultVO.setWfOper(wfOper);
        return ClientAjaxResult.success(resultVO);
    }

    private void getDesc(OrderCreateDataVO data) {
        getDescHeader(data);
    }

    /**
     * 数据检查
     */
    @PostMapping("/check")
    public ClientAjaxResult check(@RequestBody OrderCreateParamVO paramVO) {
        try {
            ClientResult checkResult = orderCreateService.check(paramVO);
            return ClientAjaxResult.success(checkResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 页面按钮操作(保存草稿，提交，同意，驳回...)
     */
    @PostMapping("/save")
    public ClientAjaxResult saveData(@RequestBody OrderCreateParamVO paramVO) {
        try {
            ClientWfReturn clientWfReturn = orderCreateService.saveData(paramVO);
            return ClientAjaxResult.success(clientWfReturn);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 下达生产订单
     */
    @GetMapping("/release/{applyId}")
    public ClientAjaxResult release(@PathVariable("applyId") String applyId) {
        try {
            ClientResult clientResult = orderCreateSapService.releaseOrderCreate(applyId);
            if (CollectionUtils.isEmpty(clientResult.getRetMsgBox())) {
                ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("2", "", "S", "无需下达");
                clientResult.getRetMsgBox().add(clientResultMsgBox);
            }
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 关闭/取消关闭生产订单
     * @param ztype C技术关闭 / R取消关闭
     */
    @GetMapping("/close/{applyId}")
    public ClientAjaxResult close(@PathVariable("applyId") String applyId, @RequestParam("ztype") String ztype) {
        try {
            ClientResult clientResult = orderCreateSapService.closeOrderCreate(applyId, ztype);
            if (CollectionUtils.isEmpty(clientResult.getRetMsgBox())) {
                String msg = "R".equals(ztype) ? "无需取消关闭" : "无需关闭";
                ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("2", "", "S", msg);
                clientResult.getRetMsgBox().add(clientResultMsgBox);
            }
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 同步SAP
     */
    @GetMapping("/syncSap/{applyId}")
    public ClientAjaxResult genSapDoc(@PathVariable("applyId") String applyId) {
        try {
            ClientResult clientResult = orderCreateSapService.genSapOrderCreate(applyId);
            if (CollectionUtils.isEmpty(clientResult.getRetMsgBox())) {
                ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("2", "", "S", "无需同步");
                clientResult.getRetMsgBox().add(clientResultMsgBox);
            }
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    private void getDescList(List<OrderCreateHeader> headerList) {
        if (!CollectionUtils.isEmpty(headerList)) {
            java.util.List<String> codes = new java.util.ArrayList<>();
            for (OrderCreateHeader header : headerList) {
                if (header.getMtlCode() != null && !header.getMtlCode().isEmpty()) {
                    codes.add(header.getMtlCode());
                }
            }
            java.util.Map<String, String> nameMap = mtlService.getMtlNameMap(codes);
            for (OrderCreateHeader header : headerList) {
                header.setBussId(header.getApplyId());
                header.setMtlName(nameMap.get(header.getMtlCode()));
                header.setFactName(clientCore.mdm.getDescA("CORE_FACT", header.getFact(), "0"));
                header.setOrderTypeName(clientCore.mdm.getDescA("Z000_PP_SCDDCJ_ORDER_TYPE", header.getOrderType(), "0"));
                header.setUnitName(clientCore.mdm.getDescA("CORE_UNIT", header.getUnit(), "0"));
                header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "0"));
                header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "0"));
                header.setBstpName(clientCore.mdm.getDescA("CORE_BSTP", header.getBstp(), "0"));
                header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "0"));
                header.setCreateByName(clientCore.mdm.getDescA("CORE_USER", header.getCreateBy(), "0"));
                header.setUpdateByName(clientCore.mdm.getDescA("CORE_USER", header.getUpdateBy(), "0"));
                header.setReleasedName(clientCore.mdm.getDescA("Z000_TF_FLAG", header.getReleased(), "0"));
                header.setClosedName(clientCore.mdm.getDescA("Z000_TF_FLAG", header.getClosed(), "0"));
            }
        }
    }

    private void getDescHeader(OrderCreateDataVO data) {
        if (data.getHeader() != null) {
            OrderCreateHeader header = data.getHeader();
            if (header.getMtlCode() != null && !header.getMtlCode().isEmpty()) {
                java.util.Map<String, String> nameMap = mtlService.getMtlNameMap(java.util.Collections.singletonList(header.getMtlCode()));
                header.setMtlName(nameMap.get(header.getMtlCode()));
            }
            header.setFactName(clientCore.mdm.getDescA("CORE_FACT", header.getFact(), "1"));
            header.setOrderTypeName(clientCore.mdm.getDescA("Z000_PP_SCDDCJ_ORDER_TYPE", header.getOrderType(), "1"));
            header.setUnitName(clientCore.mdm.getDescA("CORE_UNIT", header.getUnit(), "1"));
            header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "1"));
            header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "1"));
            header.setBstpName(clientCore.mdm.getDescA("CORE_BSTP", header.getBstp(), "1"));
            header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "1"));
        }
    }

    private static void setWfOper(OrderCreateDataVO data, ClientWfOper wfOper) {
        String bussStatus = data.getHeader().getBussStatus();
        if ("D".equals(bussStatus)) {
            wfOper.getWfBtn().setBtn_delete("V");
            wfOper.getWfBtn().setBtn_revoke("V");
        }
    }
}

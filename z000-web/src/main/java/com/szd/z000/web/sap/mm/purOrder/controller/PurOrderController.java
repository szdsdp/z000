package com.szd.z000.web.sap.mm.purOrder.controller;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.*;
import com.szd.core.client.domain.wf.ClientWfOper;
import com.szd.core.client.domain.wf.ClientWfReturn;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.service.ClientBaseController;
import com.szd.z000.base.orm.domain.mm.purOrder.PurOrderHeader;
import com.szd.z000.base.orm.domain.mm.purOrder.PurOrderItem;
import com.szd.z000.web.sap.mm.purOrder.domain.PurOrderDataVO;
import com.szd.z000.web.sap.mm.purOrder.domain.PurOrderParamVO;
import com.szd.z000.web.sap.mm.purOrder.domain.PurOrderResultVO;
import com.szd.core.client.util.ClientUtilString;
import com.szd.z000.web.sap.mm.purOrder.service.PurOrderSapService;
import com.szd.z000.web.sap.mm.purOrder.service.PurOrderService;
import com.szd.z000.web.sap.mm.purStore.service.PurStoreItemService;
import com.szd.z000.web.sap.mdm.mtl.service.MtlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 采购订单接口
 */
@Slf4j
@RestController
@RequestMapping("/z000/mm/purOrder")
public class PurOrderController extends ClientBaseController {
    @Resource
    private PurOrderService purOrderService;
    @Resource
    private PurOrderSapService purOrderSapService;
    @Resource
    private PurStoreItemService purStoreItemService;
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
        List<PurOrderHeader> list = purOrderService.selectList(param);
        getDescList(list);
        return getPageTable(list);
    }

    /**
     * 页面查询(包含业务数据、流程按钮、字段状态)
     */
    @GetMapping("/get")
    public ClientAjaxResult getData(String bussId, String userId, String option) {
        // 业务数据
        PurOrderDataVO data = purOrderService.selectData(bussId);
        // 添加描述
        getDesc(data);
        // 流程操作授权
        ClientWfOper wfOper = clientCore.wf.wfInit(bussId, userId, option);
        // 设置工作流按钮状态
        setWfOper(data, wfOper);
        // 返回
        PurOrderResultVO resultVO = new PurOrderResultVO();
        resultVO.setData(data);
        resultVO.setWfOper(wfOper);
        return ClientAjaxResult.success(resultVO);
    }

    private void getDesc(PurOrderDataVO data) {
        getDescHeader(data);
        getDescItemList(data);
    }

    private void getDescItemList(PurOrderDataVO data) {
        if (!CollectionUtils.isEmpty(data.getItemList())) {
            java.util.List<String> codes = new java.util.ArrayList<>();
            for (PurOrderItem item : data.getItemList()) {
                if (item.getMtlCode() != null && !item.getMtlCode().isEmpty()) {
                    codes.add(item.getMtlCode());
                }
            }
            java.util.Map<String, String> nameMap = mtlService.getMtlNameMap(codes);
            for (PurOrderItem item : data.getItemList()) {
                item.setMtlName(nameMap.get(item.getMtlCode()));
                item.setFactName(clientCore.mdm.getDescA("CORE_FACT", item.getFact(), "1"));
            }
        }
    }

    /**
     * 数据检查
     */
    @PostMapping("/check")
    public ClientAjaxResult check(@RequestBody PurOrderParamVO paramVO) {
        try {
            ClientResult checkResult = purOrderService.check(paramVO);
            return ClientAjaxResult.success(checkResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 页面按钮操作(保存草稿，提交，同意，驳回...)
     */
    @PostMapping("/save")
    public ClientAjaxResult saveData(@RequestBody PurOrderParamVO paramVO) {
        try {
            ClientWfReturn clientWfReturn = purOrderService.saveData(paramVO);
            return ClientAjaxResult.success(clientWfReturn);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 同步SAP
     */
    @GetMapping("/syncSap/{orderId}")
    public ClientAjaxResult genSapDoc(@PathVariable("orderId") String orderId) {
        try {
            ClientResult clientResult = purOrderSapService.genSapPurOrder(orderId);
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
     * 列表描述
     */
    private void getDescList(List<PurOrderHeader> headerList) {
        if (!CollectionUtils.isEmpty(headerList)) {
            for (PurOrderHeader header : headerList) {
                header.setBussId(header.getOrderId());
                header.setVchTypeName(clientCore.mdm.getDescA("Z000_MM_VCH_TYPE", header.getVchType(), "0"));
                header.setPurOrgName(clientCore.mdm.getDescA("Z000_MDM_ORG_PUR", header.getPurOrg(), "0"));
                header.setPurGroupName(clientCore.mdm.getDescA("Z000_MM_CGDD_CGZ", header.getPurGroup(), "0"));
                header.setCurrName(clientCore.mdm.getDescA("CORE_CURR", header.getCurr(), "0"));
                header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "0"));
                header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "0"));
                header.setBstpName(clientCore.mdm.getDescA("CORE_BSTP", header.getBstp(), "0"));
                header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "0"));
                header.setCreateByName(clientCore.mdm.getDescA("CORE_USER", header.getCreateBy(), "0"));
                header.setUpdateByName(clientCore.mdm.getDescA("CORE_USER", header.getUpdateBy(), "0"));
                header.setSuppName(clientCore.mdm.getDescA("CORE_SUPP", header.getSupp(), "0"));
                // 统计关联入库单数量
                if (ClientUtilString.isNotBlank(header.getBussDocId()) && ClientUtilString.isNotBlank(header.getBstp())) {
                    header.setStoreCount(purStoreItemService.countStoreByRefDoc(header.getBussDocId(), header.getBstp(), "!E"));
                    header.setSapStoreCount(purStoreItemService.countStoreByRefDoc(header.getBussDocId(), header.getBstp(), "F"));
                }
            }
        }
    }

    /**
     * 详情描述
     */
    private void getDescHeader(PurOrderDataVO data) {
        if (data.getHeader() != null) {
            PurOrderHeader header = data.getHeader();
            header.setVchTypeName(clientCore.mdm.getDescA("Z000_MM_CGDD_CGPZLX", header.getVchType(), "1"));
            header.setPurOrgName(clientCore.mdm.getDescA("Z000_MM_CGDD_CGZZ", header.getPurOrg(), "1"));
            header.setPurGroupName(clientCore.mdm.getDescA("Z000_MM_CGDD_CGZ", header.getPurGroup(), "1"));
            header.setCurrName(clientCore.mdm.getDescA("CORE_CURR", header.getCurr(), "1"));
            header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "1"));
            header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "1"));
            header.setBstpName(clientCore.mdm.getDescA("CORE_BSTP", header.getBstp(), "1"));
            header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "1"));
        }
    }

    /**
     * 设置工作流按钮状态
     */
    private static void setWfOper(PurOrderDataVO data, ClientWfOper wfOper) {
        String bussStatus = data.getHeader().getBussStatus();
        if ("D".equals(bussStatus)) {
            wfOper.getWfBtn().setBtn_delete("V");
            wfOper.getWfBtn().setBtn_revoke("V");
        }
    }
}

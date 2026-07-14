package com.szd.z000.web.sap.mm.purApply.controller;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.*;
import com.szd.core.client.domain.wf.ClientWfOper;
import com.szd.core.client.domain.wf.ClientWfReturn;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.service.ClientBaseController;
import com.szd.z000.base.orm.domain.mm.purApply.PurApplyHeader;
import com.szd.z000.base.orm.domain.mm.purApply.PurApplyItem;
import com.szd.z000.web.sap.mm.purApply.domain.PurApplyDataVO;
import com.szd.z000.web.sap.mm.purApply.domain.PurApplyParamVO;
import com.szd.z000.web.sap.mm.purApply.domain.PurApplyResultVO;
import com.szd.z000.web.sap.mm.purApply.service.PurApplySapService;
import com.szd.z000.web.sap.mm.purApply.service.PurApplyService;
import com.szd.z000.web.sap.mdm.mtl.service.MtlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 采购申请接口
 */
@Slf4j
@RestController
@RequestMapping("/z000/mm/purApply")
public class PurApplyController extends ClientBaseController {
    @Resource
    private PurApplyService purApplyService;
    @Resource
    private PurApplySapService purApplySapService;
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
        List<PurApplyHeader> list = purApplyService.selectList(param);
        getDescList(list);
        return getPageTable(list);
    }

    /**
     * 页面查询(包含业务数据、流程按钮、字段状态)
     */
    @GetMapping("/get")
    public ClientAjaxResult getData(String bussId, String userId, String option) {
        PurApplyDataVO data = purApplyService.selectData(bussId);
        getDesc(data);
        ClientWfOper wfOper = clientCore.wf.wfInit(bussId, userId, option);
        setWfOper(data, wfOper);
        PurApplyResultVO resultVO = new PurApplyResultVO();
        resultVO.setData(data);
        resultVO.setWfOper(wfOper);
        return ClientAjaxResult.success(resultVO);
    }

    /**
     * 数据检查
     */
    @PostMapping("/check")
    public ClientAjaxResult check(@RequestBody PurApplyParamVO paramVO) {
        try {
            ClientResult checkResult = purApplyService.check(paramVO);
            return ClientAjaxResult.success(checkResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 页面按钮操作(保存草稿，提交，同意，驳回...)
     */
    @PostMapping("/save")
    public ClientAjaxResult saveData(@RequestBody PurApplyParamVO paramVO) {
        try {
            ClientWfReturn clientWfReturn = purApplyService.saveData(paramVO);
            return ClientAjaxResult.success(clientWfReturn);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 生成采购订单(从采购申请创建草稿)
     */
    @PostMapping("/createPurOrder/{applyId}")
    public ClientAjaxResult createPurOrder(@PathVariable("applyId") String applyId) {
        try {
            String purOrderId = purApplyService.createPurOrder(applyId);
            ClientResult clientResult = new ClientResult();
            clientResult.setStatus("S");
            clientResult.setDocId(purOrderId);
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
            ClientResult clientResult = purApplySapService.genSapPurApply(applyId);
            if (CollectionUtils.isEmpty(clientResult.getRetMsgBox())) {
                ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("2", "", "S", "无需同步");
                clientResult.getRetMsgBox().add(clientResultMsgBox);
            }
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    private void getDescList(List<PurApplyHeader> headerList) {
        if (!CollectionUtils.isEmpty(headerList)) {
            for (PurApplyHeader header : headerList) {
                header.setBussId(header.getApplyId());
                header.setApplyTypeName(clientCore.mdm.getDescA("Z000_MM_CGSQ_SQLX", header.getApplyType(), "0"));
                header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "0"));
                header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "0"));
                header.setBstpName(clientCore.mdm.getDescA("CORE_BSTP", header.getBstp(), "0"));
                header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "0"));
                header.setCreateByName(clientCore.mdm.getDescA("CORE_USER", header.getCreateBy(), "0"));
                header.setUpdateByName(clientCore.mdm.getDescA("CORE_USER", header.getUpdateBy(), "0"));
            }
        }
    }

    private void getDesc(PurApplyDataVO data) {
        getDescHeader(data);
        getDescItemList(data);
    }

    private void getDescHeader(PurApplyDataVO data) {
        if (data.getHeader() != null) {
            PurApplyHeader header = data.getHeader();
            header.setApplyTypeName(clientCore.mdm.getDescA("Z000_MM_CGSQ_SQLX", header.getApplyType(), "1"));
            header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "1"));
            header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "1"));
            header.setBstpName(clientCore.mdm.getDescA("CORE_BSTP", header.getBstp(), "1"));
            header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "1"));
        }
    }

    private void getDescItemList(PurApplyDataVO data) {
        if (!CollectionUtils.isEmpty(data.getItemList())) {
            java.util.List<String> codes = new java.util.ArrayList<>();
            for (PurApplyItem item : data.getItemList()) {
                if (item.getMtlCode() != null && !item.getMtlCode().isEmpty()) {
                    codes.add(item.getMtlCode());
                }
            }
            java.util.Map<String, String> nameMap = mtlService.getMtlNameMap(codes);
            for (PurApplyItem item : data.getItemList()) {
                item.setMtlName(nameMap.get(item.getMtlCode()));
                item.setFactName(clientCore.mdm.getDescA("CORE_FACT", item.getFact(), "1"));
                item.setStoreName(clientCore.mdm.getDescA("CORE_STORE", item.getStore(), "1"));
                item.setMtlGroupName(clientCore.mdm.getDescA("Z000_MDM_MTL_GROUP", item.getMtlGroup(), "1"));
            }
        }
    }

    private static void setWfOper(PurApplyDataVO data, ClientWfOper wfOper) {
        String bussStatus = data.getHeader().getBussStatus();
        if ("D".equals(bussStatus)) {
            wfOper.getWfBtn().setBtn_delete("V");
            wfOper.getWfBtn().setBtn_revoke("V");
        }
    }
}

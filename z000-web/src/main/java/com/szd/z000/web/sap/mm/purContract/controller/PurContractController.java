package com.szd.z000.web.sap.mm.purContract.controller;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.*;
import com.szd.core.client.domain.wf.ClientWfOper;
import com.szd.core.client.domain.wf.ClientWfReturn;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.service.ClientBaseController;
import com.szd.z000.base.orm.domain.mm.purContract.PurContractHeader;
import com.szd.z000.base.orm.domain.mm.purContract.PurContractItem;
import com.szd.z000.web.sap.mm.purContract.domain.PurContractDataVO;
import com.szd.z000.web.sap.mm.purContract.domain.PurContractParamVO;
import com.szd.z000.web.sap.mm.purContract.domain.PurContractResultVO;
import com.szd.z000.web.sap.mm.purContract.service.PurContractSapService;
import com.szd.z000.web.sap.mm.purContract.service.PurContractService;
import com.szd.z000.web.sap.mdm.mtl.service.MtlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 采购合同接口
 */
@Slf4j
@RestController
@RequestMapping("/z000/mm/purContract")
public class PurContractController extends ClientBaseController {
    @Resource
    private PurContractService purContractService;
    @Resource
    private PurContractSapService purContractSapService;
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
        List<PurContractHeader> list = purContractService.selectList(param);
        getDescList(list);
        return getPageTable(list);
    }

    /**
     * 页面查询(包含业务数据、流程按钮、字段状态)
     */
    @GetMapping("/get")
    public ClientAjaxResult getData(String bussId, String userId, String option) {
        PurContractDataVO data = purContractService.selectData(bussId);
        getDesc(data);
        ClientWfOper wfOper = clientCore.wf.wfInit(bussId, userId, option);
        setWfOper(data, wfOper);
        PurContractResultVO resultVO = new PurContractResultVO();
        resultVO.setData(data);
        resultVO.setWfOper(wfOper);
        return ClientAjaxResult.success(resultVO);
    }

    /**
     * 数据检查
     */
    @PostMapping("/check")
    public ClientAjaxResult check(@RequestBody PurContractParamVO paramVO) {
        try {
            ClientResult checkResult = purContractService.check(paramVO);
            return ClientAjaxResult.success(checkResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 页面按钮操作(保存草稿，提交，同意，驳回...)
     */
    @PostMapping("/save")
    public ClientAjaxResult saveData(@RequestBody PurContractParamVO paramVO) {
        try {
            ClientWfReturn clientWfReturn = purContractService.saveData(paramVO);
            return ClientAjaxResult.success(clientWfReturn);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 同步SAP
     */
    @GetMapping("/syncSap/{contractId}")
    public ClientAjaxResult genSapDoc(@PathVariable("contractId") String contractId) {
        try {
            ClientResult clientResult = purContractSapService.genSapPurContract(contractId);
            if (CollectionUtils.isEmpty(clientResult.getRetMsgBox())) {
                ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("2", "", "S", "无需同步");
                clientResult.getRetMsgBox().add(clientResultMsgBox);
            }
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    private void getDescList(List<PurContractHeader> headerList) {
        if (!CollectionUtils.isEmpty(headerList)) {
            for (PurContractHeader header : headerList) {
                header.setBussId(header.getContractId());
                header.setTypeName(clientCore.mdm.getDescA("Z000_MM_CGHT_LX", header.getType(), "0"));
                header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "0"));
                header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "0"));
                header.setBstpName(clientCore.mdm.getDescA("CORE_BSTP", header.getBstp(), "0"));
                header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "0"));
                header.setCreateByName(clientCore.mdm.getDescA("CORE_USER", header.getCreateBy(), "0"));
                header.setUpdateByName(clientCore.mdm.getDescA("CORE_USER", header.getUpdateBy(), "0"));
            }
        }
    }

    private void getDesc(PurContractDataVO data) {
        getDescHeader(data);
        getDescItemList(data);
    }

    private void getDescHeader(PurContractDataVO data) {
        if (data.getHeader() != null) {
            PurContractHeader header = data.getHeader();
            header.setTypeName(clientCore.mdm.getDescA("Z000_MM_CGHT_LX", header.getType(), "1"));
            header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "1"));
            header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "1"));
            header.setBstpName(clientCore.mdm.getDescA("CORE_BSTP", header.getBstp(), "1"));
            header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "1"));
        }
    }

    private void getDescItemList(PurContractDataVO data) {
        if (!CollectionUtils.isEmpty(data.getItemList())) {
            java.util.List<String> codes = new java.util.ArrayList<>();
            for (PurContractItem item : data.getItemList()) {
                if (item.getMtlCode() != null && !item.getMtlCode().isEmpty()) {
                    codes.add(item.getMtlCode());
                }
            }
            java.util.Map<String, String> nameMap = mtlService.getMtlNameMap(codes);
            for (PurContractItem item : data.getItemList()) {
                item.setMtlName(nameMap.get(item.getMtlCode()));
                item.setFactName(clientCore.mdm.getDescA("CORE_FACT", item.getFact(), "1"));
                item.setStoreName(clientCore.mdm.getDescA("CORE_STORE", item.getStore(), "1"));
                item.setMtlGroupName(clientCore.mdm.getDescA("Z000_MDM_MTL_GROUP", item.getMtlGroup(), "1"));
            }
        }
    }

    private static void setWfOper(PurContractDataVO data, ClientWfOper wfOper) {
        String bussStatus = data.getHeader().getBussStatus();
        if ("D".equals(bussStatus)) {
            wfOper.getWfBtn().setBtn_delete("V");
            wfOper.getWfBtn().setBtn_revoke("V");
        }
    }
}

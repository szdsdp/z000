package com.szd.z000.web.sap.mdm.mtlcj.controller;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.*;
import com.szd.core.client.domain.wf.ClientWfOper;
import com.szd.core.client.domain.wf.ClientWfReturn;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.service.ClientBaseController;
import com.szd.z000.base.orm.domain.mdm.mtlcj.MtlcjBaseView;
import com.szd.z000.base.orm.domain.mdm.mtlcj.MtlcjHeader;
import com.szd.z000.base.orm.domain.mdm.mtlcj.MtlcjPurView;
import com.szd.z000.web.sap.mdm.mtlcj.domain.MtlcjDataVO;
import com.szd.z000.web.sap.mdm.mtlcj.domain.MtlcjParamVO;
import com.szd.z000.web.sap.mdm.mtlcj.domain.MtlcjResultVO;
import com.szd.z000.web.sap.mdm.mtlcj.service.MtlcjSapService;
import com.szd.z000.web.sap.mdm.mtlcj.service.MtlcjService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 物料主数据创建申请接口
 */
@Slf4j
@RestController
@RequestMapping("/z000/mdm/mtlcj")
public class MtlcjController extends ClientBaseController {
    @Resource
    private MtlcjService mtlcjService;
    @Resource
    private MtlcjSapService mtlcjSapService;
    @Resource
    private ClientCore clientCore;

    /**
     * 列表查询
     */
    @PostMapping("/list")
    public ClientPageTableRet list(@RequestBody ClientRpParam param) {
        startPage(param.getPageNum(), param.getPageSize());
        List<MtlcjHeader> list = mtlcjService.selectList(param);
        getDescList(list);
        return getPageTable(list);
    }

    /**
     * 页面查询(包含业务数据、流程按钮、字段状态)
     */
    @GetMapping("/get")
    public ClientAjaxResult getData(String bussId, String userId, String option) {
        MtlcjDataVO data = mtlcjService.selectData(bussId);
        getDesc(data);
        ClientWfOper wfOper = clientCore.wf.wfInit(bussId, userId, option);
        setWfOper(data, wfOper);
        MtlcjResultVO resultVO = new MtlcjResultVO();
        resultVO.setData(data);
        resultVO.setWfOper(wfOper);
        return ClientAjaxResult.success(resultVO);
    }

    /**
     * 数据检查
     */
    @PostMapping("/check")
    public ClientAjaxResult check(@RequestBody MtlcjParamVO paramVO) {
        try {
            ClientResult checkResult = mtlcjService.check(paramVO);
            return ClientAjaxResult.success(checkResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 页面按钮操作(保存草稿、提交、同意、驳回...)
     */
    @PostMapping("/save")
    public ClientAjaxResult saveData(@RequestBody MtlcjParamVO paramVO) {
        try {
            ClientWfReturn clientWfReturn = mtlcjService.saveData(paramVO);
            return ClientAjaxResult.success(clientWfReturn);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 同步SAP
     */
    @GetMapping("/syncSap/{applyId}")
    public ClientAjaxResult syncSap(@PathVariable("applyId") String applyId) {
        try {
            ClientResult clientResult = mtlcjSapService.genSapMtlcj(applyId);
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    private void getDescList(List<MtlcjHeader> headerList) {
        if (headerList != null && !headerList.isEmpty()) {
            for (MtlcjHeader header : headerList) {
                header.setBussId(header.getApplyId());
                header.setIndSectorName(clientCore.mdm.getDescA("Z000_MDM_IND_SECTOR", header.getIndSector(), "0"));
                header.setMtlTypeName(clientCore.mdm.getDescA("Z000_MDM_MTL_TYPE", header.getMtlType(), "0"));
                header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "0"));
                header.setCreateByName(clientCore.mdm.getDescA("CORE_USER", header.getCreateBy(), "0"));
                header.setUpdateByName(clientCore.mdm.getDescA("CORE_USER", header.getUpdateBy(), "0"));
            }
        }
    }

    private void getDesc(MtlcjDataVO data) {
        if (data.getHeader() != null) {
            MtlcjHeader header = data.getHeader();
            header.setIndSectorName(clientCore.mdm.getDescA("Z000_MDM_IND_SECTOR", header.getIndSector(), "1"));
            header.setMtlTypeName(clientCore.mdm.getDescA("Z000_MDM_MTL_TYPE", header.getMtlType(), "1"));
            header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "1"));
        }
        if (data.getBaseView() != null) {
            MtlcjBaseView baseView = data.getBaseView();
            baseView.setUnitBaseName(clientCore.mdm.getDescA("CORE_UNIT", baseView.getUnitBase(), "1"));
            baseView.setMtlGroupName(clientCore.mdm.getDescA("Z000_MDM_MTL_GROUP", baseView.getMtlGroup(), "1"));
            baseView.setUnitPoName(clientCore.mdm.getDescA("CORE_UNIT", baseView.getUnitPo(), "1"));
            baseView.setUnitWtName(clientCore.mdm.getDescA("CORE_UNIT", baseView.getUnitWt(), "1"));
        }
        if (data.getPurView() != null) {
            MtlcjPurView purView = data.getPurView();
            purView.setFactName(clientCore.mdm.getDescA("CORE_FACT", purView.getFact(), "1"));
            purView.setAbcIdName(clientCore.mdm.getDescA("Z000_MDM_ABC_ID", purView.getAbcId(), "1"));
            purView.setPurGroupName(clientCore.mdm.getDescA("Z000_MM_CGDD_CGZ", purView.getPurGroup(), "1"));
            purView.setIssueUnitName(clientCore.mdm.getDescA("CORE_UNIT", purView.getIssueUnit(), "1"));
        }
    }

    private static void setWfOper(MtlcjDataVO data, ClientWfOper wfOper) {
        String bussStatus = data.getHeader().getBussStatus();
        if ("D".equals(bussStatus)) {
            wfOper.getWfBtn().setBtn_delete("V");
            wfOper.getWfBtn().setBtn_revoke("V");
        }
    }
}

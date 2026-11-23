package com.szd.z000.web.sap.mm.invCheck.controller;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.*;
import com.szd.core.client.domain.wf.ClientWfOper;
import com.szd.core.client.domain.wf.ClientWfReturn;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.service.ClientBaseController;
import com.szd.z000.base.orm.domain.mm.invCheck.InvCheckHeader;
import com.szd.z000.base.orm.domain.mm.invCheck.InvCheckItem;
import com.szd.z000.web.sap.mm.invCheck.domain.InvCheckDataVO;
import com.szd.z000.web.sap.mm.invCheck.domain.InvCheckParamVO;
import com.szd.z000.web.sap.mm.invCheck.domain.InvCheckResultVO;
import com.szd.z000.web.sap.mm.invCheck.domain.InvCheckReverseParamVO;
import com.szd.z000.web.sap.mm.invCheck.service.InvCheckSapService;
import com.szd.z000.web.sap.mm.invCheck.service.InvCheckService;
import com.szd.z000.web.sap.mdm.mtl.service.MtlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 发票校验接口
 */
@Slf4j
@RestController
@RequestMapping("/z000/mm/invCheck")
public class InvCheckController extends ClientBaseController {
    @Resource
    private InvCheckService invCheckService;
    @Resource
    private InvCheckSapService invCheckSapService;
    @Resource
    private MtlService mtlService;
    @Resource
    private ClientCore clientCore;

    @PostMapping("/list")
    public ClientPageTableRet list(@RequestBody ClientRpParam param) {
        startPage(param.getPageNum(), param.getPageSize());
        List<InvCheckHeader> list = invCheckService.selectList(param);
        getDescList(list);
        return getPageTable(list);
    }

    /**
     * 页面查询(包含业务数据、流程按钮、字段状态)
     */
    @GetMapping("/get")
    public ClientAjaxResult getData(String bussId, String userId, String option) {
        InvCheckDataVO data = invCheckService.selectData(bussId);
        getDesc(data);
        ClientWfOper wfOper = clientCore.wf.wfInit(bussId, userId, option);
        setWfOper(data, wfOper);
        InvCheckResultVO resultVO = new InvCheckResultVO();
        resultVO.setData(data);
        resultVO.setWfOper(wfOper);
        return ClientAjaxResult.success(resultVO);
    }

    /**
     * 数据检查
     */
    @PostMapping("/check")
    public ClientAjaxResult check(@RequestBody InvCheckParamVO paramVO) {
        try {
            ClientResult checkResult = invCheckService.check(paramVO);
            return ClientAjaxResult.success(checkResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 页面按钮操作(保存草稿，提交，同意，驳回。。。)
     */
    @PostMapping("/save")
    public ClientAjaxResult saveData(@RequestBody InvCheckParamVO paramVO) {
        try {
            ClientWfReturn clientWfReturn = invCheckService.saveData(paramVO);
            return ClientAjaxResult.success(clientWfReturn);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 同步SAP
     */
    @GetMapping("/syncSap/{invId}")
    public ClientAjaxResult genSapDoc(@PathVariable("invId") String invId) {
        try {
            ClientResult clientResult = invCheckSapService.genSapInvCheck(invId);
            if (CollectionUtils.isEmpty(clientResult.getRetMsgBox())) {
                ClientResultMsgBox msgBox = new ClientResultMsgBox("2", "", "S", "无需同步");
                clientResult.getRetMsgBox().add(msgBox);
            }
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 冲销SAP
     */
    @PostMapping("/reverse")
    public ClientAjaxResult reverse(@RequestBody InvCheckReverseParamVO param) {
        try {
            ClientResult clientResult = invCheckSapService.reverseSapInvCheck(param.getInvId());
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 从物料凭证创建发票校验(生成A状态草稿)
     */
    @PostMapping("/createFromCgrk")
    public ClientAjaxResult createFromCgrk(@RequestBody InvCheckParamVO paramVO) {
        try {
            // 创建发票校验草稿（含重复校验）
            String invCheckId = invCheckService.createFromCgrk(paramVO);
            // 返回单据ID供前端跳转
            ClientResult clientResult = new ClientResult();
            clientResult.setStatus("S");
            clientResult.setDocId(invCheckId);
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    private void getDesc(InvCheckDataVO data) {
        if (data.getHeader() != null) {
            InvCheckHeader header = data.getHeader();
            header.setCurrName(clientCore.mdm.getDescA("CORE_CURR", header.getCurr(), "1"));
            header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "1"));
            header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "1"));
            header.setBstpName(clientCore.mdm.getDescA("CORE_BSTP", header.getBstp(), "1"));
            header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "1"));
        }
        getDescItemList(data);
    }

    private void getDescItemList(InvCheckDataVO data) {
        if (!CollectionUtils.isEmpty(data.getItemList())) {
            List<String> codes = new ArrayList<>();
            for (InvCheckItem item : data.getItemList()) {
                if (item.getMtlCode() != null && !item.getMtlCode().isEmpty()) {
                    codes.add(item.getMtlCode());
                }
            }
            Map<String, String> nameMap = mtlService.getMtlNameMap(codes);
            for (InvCheckItem item : data.getItemList()) {
                item.setMtlName(nameMap.get(item.getMtlCode()));
            }
        }
    }

    private void getDescList(List<InvCheckHeader> headerList) {
        if (!CollectionUtils.isEmpty(headerList)) {
            for (InvCheckHeader header : headerList) {
                header.setBussId(header.getInvId());
                header.setCurrName(clientCore.mdm.getDescA("CORE_CURR", header.getCurr(), "0"));
                header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "0"));
                header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "0"));
                header.setBstpName(clientCore.mdm.getDescA("CORE_BSTP", header.getBstp(), "0"));
                header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "0"));
                header.setCreateByName(clientCore.mdm.getDescA("CORE_USER", header.getCreateBy(), "0"));
                header.setUpdateByName(clientCore.mdm.getDescA("CORE_USER", header.getUpdateBy(), "0"));
            }
        }
    }

    private static void setWfOper(InvCheckDataVO data, ClientWfOper wfOper) {
        String bussStatus = data.getHeader().getBussStatus();
        if ("D".equals(bussStatus)) {
            wfOper.getWfBtn().setBtn_delete("V");
            wfOper.getWfBtn().setBtn_revoke("V");
        }
    }
}

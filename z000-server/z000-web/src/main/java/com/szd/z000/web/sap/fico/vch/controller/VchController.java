package com.szd.z000.web.sap.fico.vch.controller;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.*;
import com.szd.core.client.domain.vch.ClientVch;
import com.szd.core.client.domain.wf.ClientWfOper;
import com.szd.core.client.domain.wf.ClientWfReturn;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.service.ClientBaseController;
import com.szd.z000.base.orm.domain.fico.vch.VchHeader;
import com.szd.z000.base.orm.domain.fico.vch.VchItem;
import com.szd.z000.base.orm.domain.fico.vch.VchAdjust;
import com.szd.z000.web.sap.fico.vch.domain.VchDataVO;
import com.szd.z000.web.sap.fico.vch.domain.VchParamVO;
import com.szd.z000.web.sap.fico.vch.domain.VchResultVO;
import com.szd.z000.web.sap.fico.vch.service.VchAdjustService;
import com.szd.z000.web.sap.fico.vch.service.VchSapService;
import com.szd.z000.web.sap.fico.vch.service.VchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 费用报账接口
 */
@Slf4j
@RestController
@RequestMapping("/z000/fi/vch")
public class VchController extends ClientBaseController {
    @Resource
    private VchService vchService;
    @Resource
    private VchSapService vchSapService;
    @Resource
    private VchAdjustService vchAdjustService;
    @Resource
    private ClientCore clientCore;

    /**
     * 列表查询
     */
    @PostMapping("/list")
    public ClientPageTableRet list(@RequestBody ClientRpParam param) {
        startPage(param.getPageNum(), param.getPageSize());
        List<VchHeader> list = vchService.selectList(param);
        getDescList(list);
        return getPageTable(list);
    }

    /**
     * 页面查询(包含业务数据、流程按钮、字段状态)
     */
    @GetMapping("/get")
    public ClientAjaxResult getData(String bussId, String userId, String option) {
        VchDataVO data = vchService.selectData(bussId);
        getDesc(data);
        ClientWfOper wfOper = clientCore.wf.wfInit(bussId, userId, option);
        setWfOper(data, wfOper);
        VchResultVO resultVO = new VchResultVO();
        resultVO.setData(data);
        resultVO.setWfOper(wfOper);
        return ClientAjaxResult.success(resultVO);
    }

    /**
     * 数据检查
     */
    @PostMapping("/check")
    public ClientAjaxResult check(@RequestBody VchParamVO paramVO) {
        try {
            ClientResult checkResult = vchService.check(paramVO);
            return ClientAjaxResult.success(checkResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 页面按钮操作(保存草稿，提交，同意，驳回...)
     */
    @PostMapping("/save")
    public ClientAjaxResult saveData(@RequestBody VchParamVO paramVO) {
        try {
            ClientWfReturn clientWfReturn = vchService.saveData(paramVO);
            return ClientAjaxResult.success(clientWfReturn);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 同步SAP
     */
    @GetMapping("/syncSap/{costId}")
    public ClientAjaxResult genSapDoc(@PathVariable("costId") String costId) {
        try {
            ClientResult clientResult = vchSapService.genSapVch(costId);
            if (CollectionUtils.isEmpty(clientResult.getRetMsgBox())) {
                ClientResultMsgBox msgBox = new ClientResultMsgBox("2", "", "S", clientResult.getMsg());
                clientResult.getRetMsgBox().add(msgBox);
            }
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }


    /**
     * 凭证预览
     */
    @GetMapping("/genVchView/{bussId}")
    public ClientAjaxResult genVchView(@PathVariable("bussId") String bussId) {
        ClientResult clientResult = new ClientResult();
        ClientVch clientVch = vchSapService.vchPrepare(bussId, clientResult);
        return ClientAjaxResult.success(clientVch);
    }

    private void getDescList(List<VchHeader> headerList) {
        if (!CollectionUtils.isEmpty(headerList)) {
            for (VchHeader header : headerList) {
                header.setBussId(header.getCostId());
                header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "0"));
                header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "0"));
                header.setBstpName(clientCore.mdm.getDescA("CORE_BSTP", header.getBstp(), "0"));
                header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "0"));
                header.setCreateByName(clientCore.mdm.getDescA("CORE_USER", header.getCreateBy(), "0"));
                header.setUpdateByName(clientCore.mdm.getDescA("CORE_USER", header.getUpdateBy(), "0"));
            }
        }
    }

    private void getDesc(VchDataVO data) {
        getDescHeader(data);
        getDescItem(data);
        getDescAdjustList(data);
    }

    private void getDescItem(VchDataVO data) {
        if (data.getItemList() != null) {
            for (VchItem item : data.getItemList()) {
                item.setBsublName(clientCore.mdm.getDescA("CORE_BSUB", item.getBsubl(), "1"));
            }
        }
    }

    private void getDescHeader(VchDataVO data) {
        if (data.getHeader() != null) {
            VchHeader header = data.getHeader();
            header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "1"));
            header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "1"));
            header.setBstpName(clientCore.mdm.getDescA("CORE_BSTP", header.getBstp(), "1"));
            header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "1"));
        }
    }

    private void getDescAdjustList(VchDataVO data) {
        if (data.getHeader() != null && !CollectionUtils.isEmpty(data.getAdjustList())) {
            VchHeader header = data.getHeader();
            vchAdjustService.getDesc(data.getAdjustList(), header.getCmpy());
        }
    }

    private static void setWfOper(VchDataVO data, ClientWfOper wfOper) {
        String bussStatus = data.getHeader().getBussStatus();
        if ("D".equals(bussStatus)) {
            wfOper.getWfBtn().setBtn_delete("V");
            wfOper.getWfBtn().setBtn_revoke("V");
        }
    }
}

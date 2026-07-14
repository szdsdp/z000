package com.szd.z000.web.sap.mdm.cust.controller;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.*;
import com.szd.core.client.domain.wf.ClientWfOper;
import com.szd.core.client.domain.wf.ClientWfReturn;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.service.ClientBaseController;
import com.szd.z000.base.orm.domain.mdm.cust.MdmCustCmpy;
import com.szd.z000.base.orm.domain.mdm.cust.MdmCustHeader;
import com.szd.z000.web.sap.mdm.cust.domain.MdmCustDataVO;
import com.szd.z000.web.sap.mdm.cust.domain.MdmCustParamVO;
import com.szd.z000.web.sap.mdm.cust.domain.MdmCustResultVO;
import com.szd.z000.web.sap.mdm.cust.service.MdmCustSapService;
import com.szd.z000.web.sap.mdm.cust.service.MdmCustService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 客商主数据接口
 */
@Slf4j
@RestController
@RequestMapping("/z000/mdm/cust")
public class MdmCustController extends ClientBaseController {
    @Resource
    private MdmCustService mdmCustService;
    @Resource
    private MdmCustSapService mdmCustSapService;
    @Resource
    private ClientCore clientCore;

    @PostMapping("/list")
    public ClientPageTableRet list(@RequestBody ClientRpParam param) {
        startPage(param.getPageNum(), param.getPageSize());
        List<MdmCustHeader> list = mdmCustService.selectList(param);
        getDescList(list);
        return getPageTable(list);
    }

    /**
     * 页面查询(包含业务数据、流程按钮、字段状态)
     */
    @GetMapping("/get")
    public ClientAjaxResult getData(String bussId, String userId, String option) {
        // 业务数据
        MdmCustDataVO data = mdmCustService.selectData(bussId);
        // 添加描述
        getDesc(data);
        // 流程操作授权
        ClientWfOper wfOper = clientCore.wf.wfInit(bussId, userId, option);
        // 设置工作流按钮状态
        setWfOper(data, wfOper);
        // 返回
        MdmCustResultVO dataResultVO = new MdmCustResultVO();
        dataResultVO.setData(data);
        dataResultVO.setWfOper(wfOper);
        return ClientAjaxResult.success(dataResultVO);
    }

    /**
     * 数据检查
     */
    @PostMapping("/check")
    public ClientAjaxResult check(@RequestBody MdmCustParamVO mdmCustParamVO) {
        try {
            ClientResult checkResult = mdmCustService.check(mdmCustParamVO);
            return ClientAjaxResult.success(checkResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 页面按钮操作(保存草稿，提交，同意，驳回。。。)
     */
    @PostMapping("/save")
    public ClientAjaxResult saveData(@RequestBody MdmCustParamVO mdmCustParamVO) {
        try {
            ClientWfReturn clientWfReturn = mdmCustService.saveData(mdmCustParamVO);
            return ClientAjaxResult.success(clientWfReturn);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * SAP客商验重
     */
    @PostMapping("/checkSap")
    public ClientAjaxResult checkSap(@RequestBody MdmCustHeader header) {
        try {
            ClientResult clientResult = mdmCustSapService.checkSapCust(header);
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 同步SAP
     */
    @GetMapping("/syncSap/{bussId}")
    public ClientAjaxResult genSapDoc(@PathVariable("bussId") String bussId) {
        try {
            ClientResult clientResult = mdmCustSapService.genSapCust(bussId);
            if (CollectionUtils.isEmpty(clientResult.getRetMsgBox())) {
                ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("2", "", "S", "无需同步");
                clientResult.getRetMsgBox().add(clientResultMsgBox);
            }
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    private void getDescList(List<MdmCustHeader> custHeaderList) {
        if (!CollectionUtils.isEmpty(custHeaderList)) {
            custHeaderList.forEach(header -> {
                header.setBussId(header.getApplyId());
                header.setCustGrpName(clientCore.mdm.getDescA("Z000_MDM_CUST_BUSS_GROUP", header.getCustGrp(), "0"));
                header.setPartnerTypeName(clientCore.mdm.getDescA("Z000_MDM_CUST_BUSS_TYPE", header.getPartnerType(), "0"));
                header.setRiskClassName(clientCore.mdm.getDescA("Z000_MDM_CUST_RISK", header.getRiskClass(), "0"));
                header.setCtryName(clientCore.mdm.getDescA("CORE_CTRY", header.getCtry(), "0"));
                header.setProvinceName(header.getProvince() + "." + clientCore.mdm.getDescA("CORE_PROV", header.getProvinceCore(), "1"));
                header.setCityName(clientCore.mdm.getDescA("CORE_CITY", header.getCity(), "0"));
                header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "0"));
                header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "0"));
                header.setBstpName(clientCore.mdm.getDescA("CORE_BSTP", header.getBstp(), "0"));
                header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "0"));
                header.setCreateByName(clientCore.mdm.getDescA("CORE_USER", header.getCreateBy(), "0"));
                header.setUpdateByName(clientCore.mdm.getDescA("CORE_USER", header.getUpdateBy(), "0"));
            });
        }
    }

    private void getDesc(MdmCustDataVO data) {
        getDescHeader(data.getHeader());
        getDescItemCmpy(data.getCmpyKList());
        getDescItemCmpy(data.getCmpyDList());
//        getDescItemSale(data.getSaleList());
    }

    private void getDescHeader(MdmCustHeader header) {
        if (header != null) {
            header.setCtryName(clientCore.mdm.getDescA("CORE_CTRY", header.getCtry(), "1"));
            header.setProvinceName(clientCore.mdm.getDescA("CORE_PROV", header.getProvinceCore(), "1"));
            header.setCityName(clientCore.mdm.getDescA("CORE_CITY", header.getCity(), "1"));

            header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "1"));
            header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "1"));
            header.setBstpName(clientCore.mdm.getDescA("CORE_BSTP", header.getBstp(), "1"));
            if (header.getCustFlagDB() != null) {
                header.setCustFlag(Arrays.stream(header.getCustFlagDB().split(",")).map(String::valueOf).collect(Collectors.toList()));
            }
        }
    }

    private void getDescItemCmpy(List<MdmCustCmpy> itemList) {
        if (!CollectionUtils.isEmpty(itemList)) {
            itemList.forEach(item -> {
                item.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", item.getCmpy(), "1"));
            });
        }
    }

//    private void getDescItemSale(List<SapCustSale> itemList) {
//        if (!CollectionUtils.isEmpty(itemList)) {
//            List<AsmCust> custList = asmMdmCustService.selectListAll();
//            for (SapCustSale item : itemList) {
//                if (!CollectionUtils.isEmpty(item.getPartnerList())) {
//                    for (SapCustSalePartner partner : item.getPartnerList()) {
//                        if (ClientUtilString.isNotBlank(partner.getPartnerCode())) {
//                            Optional<AsmCust> first = custList.stream().filter(cust -> cust.getCustCode().equals(partner.getPartnerCode())).findFirst();
//                            partner.setPartnerName(first.isPresent() ? first.get().getCustName() : "");
//                        }
//                    }
//                }
//            }
//        }
//    }

    /***
     * 设置工作流按钮状态
     */
    private static void setWfOper(MdmCustDataVO data, ClientWfOper wfOper) {
        String bussStatus = data.getHeader().getBussStatus();
        if ("D".equals(bussStatus)) {
            wfOper.getWfBtn().setBtn_delete("V");
            wfOper.getWfBtn().setBtn_revoke("V");
        }
    }
}

package com.szd.z000.web.sap.mm.purStore.controller;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.*;
import com.szd.core.client.domain.wf.ClientWfOper;
import com.szd.core.client.domain.wf.ClientWfReturn;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.security.ClientSecurity;
import com.szd.core.client.security.ClientUser;
import com.szd.core.client.service.ClientBaseController;
import com.szd.core.client.util.ClientUtilDate;
import com.szd.core.client.util.ClientUtilString;
import com.szd.z000.base.orm.domain.mm.purStore.PurStoreHeader;
import com.szd.z000.base.orm.domain.mm.purStore.PurStoreItem;
import com.szd.z000.base.orm.mapper.mm.invCheck.InvCheckHeaderMapper;
import com.szd.z000.service.common.utils.Z000Util;
import com.szd.z000.web.sap.mm.purStore.domain.PurStoreDataVO;
import com.szd.z000.web.sap.mm.purStore.domain.PurStoreParamVO;
import com.szd.z000.web.sap.mm.purStore.domain.PurStoreResultVO;
import com.szd.z000.web.sap.mm.purStore.domain.PurStoreReverseParamVO;
import com.szd.z000.web.sap.mm.purStore.service.PurStoreItemService;
import com.szd.z000.web.sap.mm.purStore.service.PurStoreSapService;
import com.szd.z000.web.sap.mm.purStore.service.PurStoreService;
import com.szd.z000.web.sap.mdm.mtl.service.MtlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 物料凭证接口
 */
@Slf4j
@RestController
@RequestMapping("/z000/mm/purStore")
public class PurStoreController extends ClientBaseController {
    @Resource
    private PurStoreService purStoreService;
    @Resource
    private PurStoreSapService purStoreSapService;
    @Resource
    private PurStoreItemService purStoreItemService;
    @Resource
    private InvCheckHeaderMapper invCheckHeaderMapper;
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
        List<PurStoreHeader> list = purStoreService.selectList(param);
        getDescList(list);
        return getPageTable(list);
    }

    /**
     * 页面查询(包含业务数据、流程按钮、字段状态)
     */
    @GetMapping("/get")
    public ClientAjaxResult getData(String bussId, String userId, String option) {
        PurStoreDataVO data = purStoreService.selectData(bussId);
        getDesc(data);
        ClientWfOper wfOper = clientCore.wf.wfInit(bussId, userId, option);
        setWfOper(data, wfOper);
        PurStoreResultVO resultVO = new PurStoreResultVO();
        resultVO.setData(data);
        resultVO.setWfOper(wfOper);
        return ClientAjaxResult.success(resultVO);
    }

    private void getDesc(PurStoreDataVO data) {
        getDescHeader(data);
        getDescItemList(data);
    }

    private void getDescItemList(PurStoreDataVO data) {
        if (!CollectionUtils.isEmpty(data.getItemList())) {
            java.util.List<String> codes = new java.util.ArrayList<>();
            for (PurStoreItem item : data.getItemList()) {
                if (item.getMtlCode() != null && !item.getMtlCode().isEmpty()) {
                    codes.add(item.getMtlCode());
                }
            }
            java.util.Map<String, String> nameMap = mtlService.getMtlNameMap(codes);
            for (PurStoreItem item : data.getItemList()) {
                item.setMtlName(nameMap.get(item.getMtlCode()));
                item.setFactName(clientCore.mdm.getDescA("CORE_FACT", item.getFact(), "1"));
                item.setUnitName(clientCore.mdm.getDescA("CORE_UNIT", item.getUnit(), "1"));
                item.setStoreName(clientCore.mdm.getDescB("CORE_STOR", item.getFact(), item.getStore(), "1"));
            }
        }
    }

    /**
     * 数据检查
     */
    @PostMapping("/check")
    public ClientAjaxResult check(@RequestBody PurStoreParamVO paramVO) {
        try {
            ClientResult checkResult = purStoreService.check(paramVO);
            return ClientAjaxResult.success(checkResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 页面按钮操作(保存草稿，提交，同意，驳回...)
     */
    @PostMapping("/save")
    public ClientAjaxResult saveData(@RequestBody PurStoreParamVO paramVO) {
        try {
            ClientWfReturn clientWfReturn = purStoreService.saveData(paramVO);
            return ClientAjaxResult.success(clientWfReturn);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 同步SAP
     */
    @GetMapping("/syncSap/{storeId}")
    public ClientAjaxResult genSapDoc(@PathVariable("storeId") String storeId) {
        try {
            ClientResult clientResult = purStoreSapService.genSapPurStore(storeId);
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
     * 从采购订单清单批量创建物料凭证(草稿状态)
     */
    @PostMapping("/createFromPurOrderList")
    public ClientAjaxResult createFromPurOrderList(@RequestBody List<String> orderIds) {
        try {
            String storeId = purStoreService.createFromPurOrderList(orderIds);
            ClientResult clientResult = new ClientResult();
            clientResult.setStatus("S");
            clientResult.setDocId(storeId);
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 冲销物料凭证
     */
    @PostMapping("/reverse")
    public ClientAjaxResult reverse(@RequestBody PurStoreReverseParamVO param) {
        try {
            ClientResult clientResult = purStoreSapService.reverseSapPurStore(param.getStoreId(), param.getBudat());
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 从采购订单创建物料凭证(生成A状态草稿)
     */
    @PostMapping("/createFromPurOrder")
    public ClientAjaxResult createFromPurOrder(@RequestBody PurStoreParamVO paramVO) {
        try {
            PurStoreDataVO data = paramVO.getData();
            PurStoreHeader header = data.getHeader();
            // 校验：订单行项目是否已生成过入库单据
//            List<String> duplicates = purStoreItemService.checkDuplicate(data.getItemList());
//            if (!duplicates.isEmpty()) {
//                throw new ClientCustomException("以下订单行已生成过入库单据，不可重复生成：" + String.join("、", duplicates));
//            }
            // 当前登录用户
            ClientUser user = ClientSecurity.getLoginUser().getUser();
            // 生成单据ID
            String bussId = clientCore.tool.getNumSeq();
            header.setStoreId(bussId);
            header.setBussId(bussId);
            header.setCreateBy(ClientSecurity.getUserId());
            header.setCreateTime(new Date());
            header.setBussStatus("A");
            header.setCmpy(user.getCmpy());
            header.setCstc(user.getCstc());
            // 根据业务类型生成标题
            String bstp = header.getBstp();
            String titlePrefix;
            if ("Z000_MM_CGRK02".equals(bstp)) {
                titlePrefix = "生产投料_";
            } else if ("Z000_MM_CGRK03".equals(bstp)) {
                titlePrefix = "生产订单入库_";
            } else if ("Z000_MM_CGRK01".equals(bstp)) {
                titlePrefix = "物料凭证_";
            } else {
                titlePrefix = "入库_";
            }
            header.setTitle(titlePrefix + ClientUtilDate.getDate("yyyy-MM-dd", new Date()));
            header.setBussDate(new Date());
            // 设置行项目号
            if (!CollectionUtils.isEmpty(data.getItemList())) {
                for (int i = 0; i < data.getItemList().size(); i++) {
                    data.getItemList().get(i).setItemNo(i + 1);
                }
            }
            Z000Util.saveBussBase(header);
            purStoreService.insert(data);
            // 返回单据ID供前端跳转
            ClientResult clientResult = new ClientResult();
            clientResult.setStatus("S");
            clientResult.setDocId(bussId);
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    private void getDescList(List<PurStoreHeader> headerList) {
        if (!CollectionUtils.isEmpty(headerList)) {
            for (PurStoreHeader header : headerList) {
                header.setBussId(header.getStoreId());
                header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "0"));
                header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "0"));
                header.setBstpName(clientCore.mdm.getDescA("CORE_BSTP", header.getBstp(), "0"));
                header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "0"));
                header.setCreateByName(clientCore.mdm.getDescA("CORE_USER", header.getCreateBy(), "0"));
                header.setUpdateByName(clientCore.mdm.getDescA("CORE_USER", header.getUpdateBy(), "0"));
                // 统计关联发票校验数量及金额
                if (ClientUtilString.isNotBlank(header.getBussDocId())) {
                    header.setInvCheckCount(invCheckHeaderMapper.countByStoreBussDocId(header.getBussDocId(), "!E"));
                    header.setInvCheckAmt(invCheckHeaderMapper.sumAmtByStoreBussDocId(header.getBussDocId(), "!E"));
                    header.setSapInvCheckCount(invCheckHeaderMapper.countByStoreBussDocId(header.getBussDocId(), "F"));
                    header.setSapInvCheckAmt(invCheckHeaderMapper.sumAmtByStoreBussDocId(header.getBussDocId(), "F"));
                }
            }
        }
    }

    private void getDescHeader(PurStoreDataVO data) {
        if (data.getHeader() != null) {
            PurStoreHeader header = data.getHeader();
            header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "1"));
            header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "1"));
            header.setBstpName(clientCore.mdm.getDescA("CORE_BSTP", header.getBstp(), "1"));
            header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "1"));
        }
    }

    private static void setWfOper(PurStoreDataVO data, ClientWfOper wfOper) {
        String bussStatus = data.getHeader().getBussStatus();
        if ("D".equals(bussStatus)) {
            wfOper.getWfBtn().setBtn_delete("V");
            wfOper.getWfBtn().setBtn_revoke("V");
        }
    }
}

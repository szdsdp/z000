package com.szd.z000.web.sap.sd.saleInv.controller;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientAjaxResult;
import com.szd.core.client.domain.common.ClientPageTableRet;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.ClientWfOper;
import com.szd.core.client.domain.wf.ClientWfReturn;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.service.ClientBaseController;
import com.szd.z000.base.orm.domain.sd.saleDelivery.SaleDeliveryHeader;
import com.szd.z000.base.orm.domain.sd.saleInv.SaleInvHeader;
import com.szd.z000.base.orm.domain.sd.saleInv.SaleInvItem;
import com.szd.z000.web.sap.sd.saleDelivery.service.SaleDeliveryHeaderService;
import com.szd.z000.web.sap.sd.saleInv.domain.SaleInvDataVO;
import com.szd.z000.web.sap.sd.saleInv.domain.SaleInvParamVO;
import com.szd.z000.web.sap.sd.saleInv.domain.SaleInvResultVO;
import com.szd.z000.web.sap.sd.saleInv.service.SaleInvSapService;
import com.szd.z000.web.sap.sd.saleInv.service.SaleInvService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 销售开票接口
 */
@Slf4j
@RestController
@RequestMapping("/z000/sd/saleInv")
public class SaleInvController extends ClientBaseController {
    @Resource
    private SaleInvService saleInvService;
    @Resource
    private SaleInvSapService saleInvSapService;
    @Resource
    private SaleDeliveryHeaderService saleDeliveryHeaderService;
    @Resource
    private ClientCore clientCore;

    /**
     * 列表查询
     */
    @PostMapping("/list")
    public ClientPageTableRet list(@RequestBody ClientRpParam param) {
        startPage(param.getPageNum(), param.getPageSize());
        List<SaleInvHeader> list = saleInvService.selectList(param);
        getDescList(list);
        return getPageTable(list);
    }

    /**
     * 页面查询
     */
    @GetMapping("/get")
    public ClientAjaxResult getData(String bussId, String userId, String option) {
        SaleInvDataVO data = saleInvService.selectData(bussId);
        getDesc(data);
        ClientWfOper wfOper = clientCore.wf.wfInit(bussId, userId, option);
        setWfOper(data, wfOper);
        SaleInvResultVO resultVO = new SaleInvResultVO();
        resultVO.setData(data);
        resultVO.setWfOper(wfOper);
        return ClientAjaxResult.success(resultVO);
    }

    /**
     * 补全描述信息
     */
    private void getDesc(SaleInvDataVO data) {
        getDescHeader(data);
        getDescItemList(data);
    }

    /**
     * 补全行项目描述（查询交货单信息）
     */
    private void getDescItemList(SaleInvDataVO data) {
        if (!CollectionUtils.isEmpty(data.getItemList())) {
            for (SaleInvItem item : data.getItemList()) {
                if (item.getVbelnVl() != null && !item.getVbelnVl().isEmpty()) {
                    SaleDeliveryHeader saleDeliveryHeader = saleDeliveryHeaderService.selectByVbeln(item.getVbelnVl());
                    if (saleDeliveryHeader != null) {
                        item.setDeliveryBussDocId(saleDeliveryHeader.getBussDocId());
                    }
                }
            }
        }
    }

    /**
     * 数据检查
     */
    @PostMapping("/check")
    public ClientAjaxResult check(@RequestBody SaleInvParamVO paramVO) {
        try {
            ClientResult checkResult = saleInvService.check(paramVO);
            return ClientAjaxResult.success(checkResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 页面按钮操作
     */
    @PostMapping("/save")
    public ClientAjaxResult saveData(@RequestBody SaleInvParamVO paramVO) {
        try {
            ClientWfReturn clientWfReturn = saleInvService.saveData(paramVO);
            return ClientAjaxResult.success(clientWfReturn);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 出具发票
     */
    @GetMapping("/issueInvoice/{invId}")
    public ClientAjaxResult issueInvoice(@PathVariable("invId") String invId) {
        try {
            ClientResult clientResult = saleInvSapService.issueInvoice(invId);
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 冲销出具发票
     */
    @GetMapping("/cancelInvoice/{invId}")
    public ClientAjaxResult cancelInvoice(@PathVariable("invId") String invId) {
        try {
            ClientResult clientResult = saleInvSapService.cancelInvoice(invId);
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 列表查询时补全抬头描述
     */
    private void getDescList(List<SaleInvHeader> headerList) {
        if (!CollectionUtils.isEmpty(headerList)) {
            for (SaleInvHeader header : headerList) {
                header.setBussId(header.getInvId());
                header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "0"));
                header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "0"));
                header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "0"));
                header.setCreateByName(clientCore.mdm.getDescA("CORE_USER", header.getCreateBy(), "0"));
                header.setUpdateByName(clientCore.mdm.getDescA("CORE_USER", header.getUpdateBy(), "0"));
            }
        }
    }

    /**
     * 详情查询时补全抬头描述
     */
    private void getDescHeader(SaleInvDataVO data) {
        if (data.getHeader() != null) {
            SaleInvHeader header = data.getHeader();
            header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "1"));
            header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "1"));
            header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "1"));
        }
    }

    /**
     * 根据业务状态设置工作流按钮
     */
    private static void setWfOper(SaleInvDataVO data, ClientWfOper wfOper) {
        String bussStatus = data.getHeader().getBussStatus();
        if ("D".equals(bussStatus)) {
            wfOper.getWfBtn().setBtn_delete("V");
            wfOper.getWfBtn().setBtn_revoke("V");
        }
    }
}

package com.szd.z000.web.sap.sd.saleOrder.controller;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.*;
import com.szd.core.client.domain.wf.ClientWfOper;
import com.szd.core.client.domain.wf.ClientWfReturn;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.security.ClientSecurity;
import com.szd.core.client.security.ClientUser;
import com.szd.core.client.service.ClientBaseController;
import com.szd.core.client.util.ClientUtilDate;
import com.szd.z000.base.orm.domain.sd.saleOrder.SaleOrderHeader;
import com.szd.z000.base.orm.domain.sd.saleOrder.SaleOrderItem;
import com.szd.z000.service.common.utils.Z000Util;
import com.szd.z000.web.sap.sd.saleOrder.domain.SaleOrderDataVO;
import com.szd.z000.web.sap.sd.saleOrder.domain.SaleOrderParamVO;
import com.szd.z000.web.sap.sd.saleOrder.domain.SaleOrderResultVO;
import com.szd.z000.web.sap.sd.saleOrder.service.SaleOrderItemService;
import com.szd.z000.web.sap.sd.saleOrder.service.SaleOrderSapService;
import com.szd.z000.web.sap.sd.saleOrder.service.SaleOrderService;
import com.szd.z000.web.sap.mdm.mtl.service.MtlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 销售订单创建接口
 */
@Slf4j
@RestController
@RequestMapping("/z000/sd/saleOrder")
public class SaleOrderController extends ClientBaseController {
    @Resource
    private SaleOrderService saleOrderService;
    @Resource
    private SaleOrderSapService saleOrderSapService;
    @Resource
    private SaleOrderItemService saleOrderItemService;
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
        List<SaleOrderHeader> list = saleOrderService.selectList(param);
        getDescList(list);
        return getPageTable(list);
    }

    /**
     * 页面查询
     */
    @GetMapping("/get")
    public ClientAjaxResult getData(String bussId, String userId, String option) {
        SaleOrderDataVO data = saleOrderService.selectData(bussId);
        getDesc(data);
        ClientWfOper wfOper = clientCore.wf.wfInit(bussId, userId, option);
        setWfOper(data, wfOper);
        SaleOrderResultVO resultVO = new SaleOrderResultVO();
        resultVO.setData(data);
        resultVO.setWfOper(wfOper);
        return ClientAjaxResult.success(resultVO);
    }

    /**
     * 补全描述信息     */
    private void getDesc(SaleOrderDataVO data) {
        getDescHeader(data);
        getDescItemList(data);
    }

    /**
     * 补全行项目的物料名称和单位名称
     */
    private void getDescItemList(SaleOrderDataVO data) {
        if (!CollectionUtils.isEmpty(data.getItemList())) {
            java.util.List<String> codes = new java.util.ArrayList<>();
            for (SaleOrderItem item : data.getItemList()) {
                if (item.getMtlCode() != null && !item.getMtlCode().isEmpty()) {
                    codes.add(item.getMtlCode());
                }
            }
            java.util.Map<String, String> nameMap = mtlService.getMtlNameMap(codes);
            for (SaleOrderItem item : data.getItemList()) {
                item.setMtlName(nameMap.get(item.getMtlCode()));
                item.setUnitName(clientCore.mdm.getDescA("CORE_UNIT", item.getUnit(), "1"));
            }
        }
    }

    /**
     * 数据检查
     */
    @PostMapping("/check")
    public ClientAjaxResult check(@RequestBody SaleOrderParamVO paramVO) {
        try {
            ClientResult checkResult = saleOrderService.check(paramVO);
            return ClientAjaxResult.success(checkResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 页面按钮操作
     */
    @PostMapping("/save")
    public ClientAjaxResult saveData(@RequestBody SaleOrderParamVO paramVO) {
        try {
            ClientWfReturn clientWfReturn = saleOrderService.saveData(paramVO);
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
            ClientResult clientResult = saleOrderSapService.genSapSaleOrder(orderId);
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
     * 列表查询时补全抬头描述信息
     */
    private void getDescList(List<SaleOrderHeader> headerList) {
        if (!CollectionUtils.isEmpty(headerList)) {
            for (SaleOrderHeader header : headerList) {
                header.setBussId(header.getOrderId());
                header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "0"));
                header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "0"));
                header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "0"));
                header.setCreateByName(clientCore.mdm.getDescA("CORE_USER", header.getCreateBy(), "0"));
                header.setUpdateByName(clientCore.mdm.getDescA("CORE_USER", header.getUpdateBy(), "0"));
            }
        }
    }

    /**
     * 详情查询时补全抬头描述信息     */
    private void getDescHeader(SaleOrderDataVO data) {
        if (data.getHeader() != null) {
            SaleOrderHeader header = data.getHeader();
            header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "1"));
            header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "1"));
            header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "1"));
            header.setSoldToName(clientCore.mdm.getDescA("CORE_CUST", header.getSoldTo(), "1"));
            header.setShipToName(clientCore.mdm.getDescA("CORE_CUST", header.getShipTo(), "1"));
            header.setBillToName(clientCore.mdm.getDescA("CORE_CUST", header.getBillTo(), "1"));
        }
    }

    /**
     * 根据业务状态设置工作流按钮可见性
     */
    private static void setWfOper(SaleOrderDataVO data, ClientWfOper wfOper) {
        String bussStatus = data.getHeader().getBussStatus();
        if ("D".equals(bussStatus)) {
            wfOper.getWfBtn().setBtn_delete("V");
            wfOper.getWfBtn().setBtn_revoke("V");
        }
    }
}

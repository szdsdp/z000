package com.szd.z000.web.sap.sd.saleDelivery.controller;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.*;
import com.szd.core.client.domain.wf.ClientWfOper;
import com.szd.core.client.domain.wf.ClientWfReturn;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.security.ClientSecurity;
import com.szd.core.client.security.ClientUser;
import com.szd.core.client.service.ClientBaseController;
import com.szd.core.client.util.ClientUtilDate;
import com.szd.z000.base.orm.domain.sd.saleDelivery.SaleDeliveryHeader;
import com.szd.z000.base.orm.domain.sd.saleDelivery.SaleDeliveryItem;
import com.szd.z000.base.orm.domain.sd.saleDelivery.SaleDeliveryPost;
import com.szd.z000.service.common.utils.Z000Util;
import com.szd.z000.web.sap.mdm.mtl.service.MtlService;
import com.szd.z000.web.sap.sd.saleDelivery.domain.SaleDeliveryDataVO;
import com.szd.z000.web.sap.sd.saleDelivery.domain.SaleDeliveryParamVO;
import com.szd.z000.web.sap.sd.saleDelivery.domain.SaleDeliveryResultVO;
import com.szd.z000.web.sap.sd.saleDelivery.service.SaleDeliveryItemService;
import com.szd.z000.web.sap.sd.saleDelivery.service.SaleDeliverySapService;
import com.szd.z000.web.sap.sd.saleDelivery.service.SaleDeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 销售交货单接口
 */
@Slf4j
@RestController
@RequestMapping("/z000/sd/saleDelivery")
public class SaleDeliveryController extends ClientBaseController {
    @Resource
    private SaleDeliveryService saleDeliveryService;
    @Resource
    private SaleDeliverySapService saleDeliverySapService;
    @Resource
    private SaleDeliveryItemService saleDeliveryItemService;
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
        List<SaleDeliveryHeader> list = saleDeliveryService.selectList(param);
        getDescList(list);
        return getPageTable(list);
    }

    /**
     * 页面查询
     */
    @GetMapping("/get")
    public ClientAjaxResult getData(String bussId, String userId, String option) {
        SaleDeliveryDataVO data = saleDeliveryService.selectData(bussId);
        getDesc(data);
        ClientWfOper wfOper = clientCore.wf.wfInit(bussId, userId, option);
        setWfOper(data, wfOper);
        SaleDeliveryResultVO resultVO = new SaleDeliveryResultVO();
        resultVO.setData(data);
        resultVO.setWfOper(wfOper);
        return ClientAjaxResult.success(resultVO);
    }

    /**
     * 补全描述信息
     */
    private void getDesc(SaleDeliveryDataVO data) {
        getDescHeader(data);
        getDescItemList(data);
    }

    /**
     * 补全行项目描述
     */
    private void getDescItemList(SaleDeliveryDataVO data) {
        if (!CollectionUtils.isEmpty(data.getItemList())) {
            List<String> codes = new ArrayList<>();
            for (SaleDeliveryItem item : data.getItemList()) {
                if (item.getMtlCode() != null && !item.getMtlCode().isEmpty()) {
                    codes.add(item.getMtlCode());
                }
            }
            Map<String, String> nameMap = mtlService.getMtlNameMap(codes);
            String deliveryId = data.getHeader().getDeliveryId();
            for (SaleDeliveryItem item : data.getItemList()) {
                item.setMtlName(nameMap.get(item.getMtlCode()));
                item.setUnitName(clientCore.mdm.getDescA("CORE_UNIT", item.getUnit(), "1"));
                // 计算未交货数量：订单数量 - 已交货总量（排除当前单据）
                if (item.getOrderQty() != null && item.getOrderNo() != null && item.getOrderItemNo() != null) {
                    BigDecimal delivered = saleDeliveryItemService.sumDlvQty(item.getOrderNo(), item.getOrderItemNo(), deliveryId);
                    item.setUndeliveredQty(item.getOrderQty().subtract(delivered));
                }
            }
        }
    }

    /**
     * 数据检查
     */
    @PostMapping("/check")
    public ClientAjaxResult check(@RequestBody SaleDeliveryParamVO paramVO) {
        try {
            ClientResult checkResult = saleDeliveryService.check(paramVO);
            return ClientAjaxResult.success(checkResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 页面按钮操作
     */
    @PostMapping("/save")
    public ClientAjaxResult saveData(@RequestBody SaleDeliveryParamVO paramVO) {
        try {
            ClientWfReturn clientWfReturn = saleDeliveryService.saveData(paramVO);
            return ClientAjaxResult.success(clientWfReturn);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 同步SAP
     */
    @GetMapping("/syncSap/{deliveryId}")
    public ClientAjaxResult genSapDoc(@PathVariable("deliveryId") String deliveryId) {
        try {
            ClientResult clientResult = saleDeliverySapService.genSapSaleDelivery(deliveryId);
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
     * 从销售订单创建交货单
     */
    @PostMapping("/createFromSaleOrder")
    public ClientAjaxResult createFromSaleOrder(@RequestBody SaleDeliveryParamVO paramVO) {
        try {
            SaleDeliveryDataVO data = paramVO.getData();
            SaleDeliveryHeader header = data.getHeader();
            ClientUser user = ClientSecurity.getLoginUser().getUser();
            String bussId = clientCore.tool.getNumSeq();
            header.setDeliveryId(bussId);
            header.setBussId(bussId);
            header.setCreateBy(ClientSecurity.getUserId());
            header.setCreateTime(new Date());
            header.setBussStatus("A");
            header.setCmpy(user.getCmpy());
            header.setCstc(user.getCstc());
            header.setTitle("销售交货单_" + ClientUtilDate.getDate("yyyy-MM-dd", new Date()));
            header.setBussDate(new Date());
            if (!CollectionUtils.isEmpty(data.getItemList())) {
                // 校验每条订单行是否已交货完成
                for (SaleDeliveryItem item : data.getItemList()) {
                    if (item.getOrderQty() != null && item.getOrderNo() != null && item.getOrderItemNo() != null) {
                        BigDecimal delivered = saleDeliveryItemService.sumDlvQty(item.getOrderNo(), item.getOrderItemNo(), null);
                        if (delivered.compareTo(item.getOrderQty()) >= 0) {
                            throw new ClientCustomException("销售订单[" + item.getOrderNo()
                                    + "]行项目[" + item.getOrderItemNo() + "]已交货完成，无法再创建交货单");
                        }
                    }
                }
                for (int i = 0; i < data.getItemList().size(); i++) {
                    data.getItemList().get(i).setItemNo(i + 1);
                }
            }
            Z000Util.saveBussBase(header);
            saleDeliveryService.insert(data);
            ClientResult clientResult = new ClientResult();
            clientResult.setStatus("S");
            clientResult.setDocId(bussId);
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 发货过账
     */
    @PostMapping("/post")
    public ClientAjaxResult postGoods(@RequestBody SaleDeliveryPost postData) {
        try {
            ClientResult clientResult = saleDeliverySapService.postGoods(postData);
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 冲销发货过账
     */
    @GetMapping("/reversePost/{deliveryId}")
    public ClientAjaxResult reversePostGoods(@PathVariable("deliveryId") String deliveryId) {
        try {
            ClientResult clientResult = saleDeliverySapService.reversePostGoods(deliveryId);
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 列表查询时补全抬头描述
     */
    private void getDescList(List<SaleDeliveryHeader> headerList) {
        if (!CollectionUtils.isEmpty(headerList)) {
            for (SaleDeliveryHeader header : headerList) {
                header.setBussId(header.getDeliveryId());
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
    private void getDescHeader(SaleDeliveryDataVO data) {
        if (data.getHeader() != null) {
            SaleDeliveryHeader header = data.getHeader();
            header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "1"));
            header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "1"));
            header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "1"));
        }
    }

    /**
     * 根据业务状态设置工作流按钮
     */
    private static void setWfOper(SaleDeliveryDataVO data, ClientWfOper wfOper) {
        String bussStatus = data.getHeader().getBussStatus();
        if ("D".equals(bussStatus)) {
            wfOper.getWfBtn().setBtn_delete("V");
            wfOper.getWfBtn().setBtn_revoke("V");
        }
    }
}

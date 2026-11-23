package com.szd.z000.web.sap.sd.saleOrder.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.util.ClientUtilDate;
import com.szd.z000.base.orm.domain.sd.saleOrder.SaleOrderHeader;
import com.szd.z000.base.orm.domain.sd.saleOrder.SaleOrderItem;
import com.szd.z000.base.orm.domain.sd.saleOrder.SaleOrderSched;
import com.szd.z000.common.sap.domain.saleOrder.*;
import com.szd.z000.common.sap.domain.service.SapSaleOrderCreateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 销售订单创建-SAP服务
 */
@Service
public class SaleOrderSapService {
    private static final Logger logger = LoggerFactory.getLogger(SaleOrderSapService.class);

    @Resource
    private SaleOrderHeaderService saleOrderHeaderService;
    @Resource
    private SaleOrderItemService saleOrderItemService;
    @Resource
    private SaleOrderSchedService saleOrderSchedService;
    @Resource
    private SapSaleOrderCreateService sapSaleOrderCreateService;
    @Resource
    private ClientCore clientCore;

    /**
     * 推送SAP创建销售订单
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClientResult genSapSaleOrder(String orderId) {
        ClientResult clientResult = new ClientResult();
        SaleOrderHeader header = saleOrderHeaderService.selectById(orderId);
        if (header == null || !"D".equals(header.getBussStatus())) {
            return clientResult;
        }
        List<SaleOrderItem> itemList = saleOrderItemService.selectList(orderId);
        List<SaleOrderSched> schedList = saleOrderSchedService.selectList(orderId);

        // 组装SAP抬头参数
        SapSaleOrderCtrHeader sapSaleOrderCtrHeader = new SapSaleOrderCtrHeader();
        sapSaleOrderCtrHeader.setIfDocno(header.getBussDocId());
        // 客户映射：Java字段 -> SAP字段
        sapSaleOrderCtrHeader.setKunnrAg(header.getSoldTo());
        sapSaleOrderCtrHeader.setKunnrWe(header.getShipTo());
        sapSaleOrderCtrHeader.setKunnrRe(header.getBillTo());
        sapSaleOrderCtrHeader.setDocType(header.getOrderType());
        sapSaleOrderCtrHeader.setSalesOrg(header.getSalesOrg());
        sapSaleOrderCtrHeader.setDistrChan(header.getDistrChan());
        sapSaleOrderCtrHeader.setDivision(header.getProdGroup());

        // 封装SAP参数-行项目
        List<SapSaleOrderCtrItem> itemListSap = new ArrayList<>();
        for (SaleOrderItem item : itemList) {
            SapSaleOrderCtrItem sapSaleOrderCtrItem = new SapSaleOrderCtrItem();
            sapSaleOrderCtrItem.setItmNumber(String.valueOf(item.getItemNo()));
            sapSaleOrderCtrItem.setMaterial(item.getMtlCode());
            sapSaleOrderCtrItem.setTargetQty(item.getEntryQnt());
            sapSaleOrderCtrItem.setTargetQu(item.getUnit());
            sapSaleOrderCtrItem.setShortText(item.getItemText());
            itemListSap.add(sapSaleOrderCtrItem);
        }

        // 封装SAP参数-交货计划
        List<SapSaleOrderCtrSched> schedListSap = new ArrayList<>();
        for (SaleOrderSched sched : schedList) {
            SapSaleOrderCtrSched sapSaleOrderCtrSched = new SapSaleOrderCtrSched();
            sapSaleOrderCtrSched.setItmNumber(String.valueOf(sched.getItemNo()));
            sapSaleOrderCtrSched.setReqDate(ClientUtilDate.dateToSap(sched.getReqDate()));
            sapSaleOrderCtrSched.setReqQty(sched.getReqQty());
            schedListSap.add(sapSaleOrderCtrSched);
        }

        // 组装SAP参数
        SapSaleOrderCtr sapSaleOrderCtr = new SapSaleOrderCtr();
        sapSaleOrderCtr.setHeader(sapSaleOrderCtrHeader);
        sapSaleOrderCtr.setItemList(itemListSap);
        sapSaleOrderCtr.setSchedList(schedListSap);

        // 调用SAP接口创建销售订单
        clientResult = sapSaleOrderCreateService.createSaleOrder(sapSaleOrderCtr, orderId);
        clientResult.setStatus(clientResult.getStatus());

        return clientResult;
    }
}

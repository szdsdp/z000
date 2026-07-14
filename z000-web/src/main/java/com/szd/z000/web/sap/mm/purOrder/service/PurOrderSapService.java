package com.szd.z000.web.sap.mm.purOrder.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.util.ClientUtilDate;
import com.szd.core.client.util.ClientUtilString;
import com.szd.z000.base.orm.domain.mm.purOrder.PurOrderHeader;
import com.szd.z000.base.orm.domain.mm.purOrder.PurOrderItem;
import com.szd.z000.base.orm.domain.mm.purOrder.PurOrderSched;
import com.szd.z000.common.sap.domain.purOrder.*;
import com.szd.z000.common.sap.domain.service.SapPurOrderCreateService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 采购订单-SAP服务
 */
@Service
public class PurOrderSapService {
    private static final Logger logger = LoggerFactory.getLogger(PurOrderSapService.class);

    @Resource
    private PurOrderHeaderService purOrderHeaderService;
    @Resource
    private PurOrderItemService purOrderItemService;
    @Resource
    private PurOrderSchedService purOrderSchedService;
    @Resource
    private SapPurOrderCreateService sapPurOrderCreateService;
    @Resource
    private ClientCore clientCore;

    /**
     * 推送SAP创建采购订单
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClientResult genSapPurOrder(String orderId) {
        ClientResult clientResult = new ClientResult();
        PurOrderHeader header = purOrderHeaderService.selectById(orderId);
        if (header == null || !"D".equals(header.getBussStatus())) {
            return clientResult;
        }
        List<PurOrderItem> itemList = purOrderItemService.selectList(orderId);
        List<PurOrderSched> schedList = purOrderSchedService.selectList(orderId);

        // 封装SAP参数-抬头
        SapPurOrderCtrHeader sapPurOrderCtrHeader = new SapPurOrderCtrHeader();
        sapPurOrderCtrHeader.setIfDocno(header.getBussDocId());
        sapPurOrderCtrHeader.setPoNumber(header.getVchNo());
        sapPurOrderCtrHeader.setCompCode(header.getCmpy());
        sapPurOrderCtrHeader.setDocType(header.getVchType());
        sapPurOrderCtrHeader.setVendor(header.getSupp());
        sapPurOrderCtrHeader.setPurchOrg(header.getPurOrg());
        sapPurOrderCtrHeader.setPurGroup(header.getPurGroup());
        sapPurOrderCtrHeader.setCurrency(header.getCurr());
        sapPurOrderCtrHeader.setEbdat(ClientUtilDate.dateToSap(header.getVchDate()));

        // 封装SAP参数-抬头文本
        SapPurOrderCtrHeadtxt headtxt = new SapPurOrderCtrHeadtxt();
        if (StringUtils.isNotBlank(header.getTextLine())) {
            headtxt.setTextLine(header.getTextLine());
        }

        // 封装SAP参数-行项目
        List<SapPurOrderCtrItem> itemListSap = new ArrayList<>();
        for (PurOrderItem item : itemList) {
            SapPurOrderCtrItem sapPurOrderCtrItem = new SapPurOrderCtrItem();
            sapPurOrderCtrItem.setPoItem(String.format("%05d", item.getItemNo() * 10));
            sapPurOrderCtrItem.setMaterial(item.getMtlCode());
            sapPurOrderCtrItem.setPlant(item.getFact());
            sapPurOrderCtrItem.setQuantity(item.getPurNum());
            sapPurOrderCtrItem.setPoUnit(item.getUnit());
            if(ClientUtilString.isNotEmpty(item.getApplyNo())){
                sapPurOrderCtrItem.setPreqNo(item.getApplyNo());
                sapPurOrderCtrItem.setPreqItem(String.format("%05d", item.getApplyNoItem() * 10));
            }
            itemListSap.add(sapPurOrderCtrItem);
        }

        // 封装SAP参数-交货计划
        List<SapPurOrderCtrSched> schedListSap = new ArrayList<>();
        for (PurOrderSched sched : schedList) {
            SapPurOrderCtrSched sapPurOrderCtrSched = new SapPurOrderCtrSched();
            sapPurOrderCtrSched.setPoItem(String.format("%05d", sched.getItemNo() * 10));
            sapPurOrderCtrSched.setSchedLine(String.format("%05d", sched.getSchedLine() * 10));
            sapPurOrderCtrSched.setDeliveryDate(ClientUtilDate.dateToSap(sched.getDeliveryDate()));
            sapPurOrderCtrSched.setQuantity(sched.getQuantity());
            schedListSap.add(sapPurOrderCtrSched);
        }

        // 组装SAP参数
        SapPurOrderCtr sapPurOrderCtr = new SapPurOrderCtr();
        sapPurOrderCtr.setHeader(sapPurOrderCtrHeader);
        sapPurOrderCtr.setHeaderTxt(headtxt);
        sapPurOrderCtr.setItemList(itemListSap);
        sapPurOrderCtr.setSchedList(schedListSap);

        // 调用SAP接口创建采购订单
        clientResult = sapPurOrderCreateService.createPurOrder(sapPurOrderCtr, orderId);
        clientResult.setStatus(clientResult.getStatus());

        return clientResult;
    }
}

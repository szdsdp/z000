package com.szd.z000.web.sap.sd.saleDelivery.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.util.ClientUtilDate;
import com.szd.z000.base.orm.domain.sd.saleDelivery.SaleDeliveryHeader;
import com.szd.z000.base.orm.domain.sd.saleDelivery.SaleDeliveryItem;
import com.szd.z000.base.orm.domain.sd.saleDelivery.SaleDeliveryPost;
import com.szd.z000.common.sap.domain.saleDelivery.*;
import com.szd.z000.common.sap.domain.service.SapSaleDeliveryCreateService;
import com.szd.z000.common.sap.domain.service.SapSaleDeliveryPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 销售交货单-SAP服务
 */
@Service
public class SaleDeliverySapService {
    private static final Logger logger = LoggerFactory.getLogger(SaleDeliverySapService.class);

    @Resource
    private SaleDeliveryHeaderService saleDeliveryHeaderService;
    @Resource
    private SaleDeliveryItemService saleDeliveryItemService;
    @Resource
    private SaleDeliveryPostService saleDeliveryPostService;
    @Resource
    private SapSaleDeliveryCreateService sapSaleDeliveryCreateService;
    @Resource
    private SapSaleDeliveryPostService sapSaleDeliveryPostService;
    @Resource
    private ClientCore clientCore;

    /**
     * 推送SAP创建销售交货单
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClientResult genSapSaleDelivery(String deliveryId) {
        ClientResult clientResult = new ClientResult();
        SaleDeliveryHeader header = saleDeliveryHeaderService.selectById(deliveryId);
        if (header == null || !"D".equals(header.getBussStatus())) {
            return clientResult;
        }
        List<SaleDeliveryItem> itemList = saleDeliveryItemService.selectList(deliveryId);

        // 组装SAP抬头参数
        SapSaleDeliveryCtrHeader sapSaleDeliveryCtrHeader = new SapSaleDeliveryCtrHeader();
        sapSaleDeliveryCtrHeader.setIfDocno(header.getBussDocId());
        sapSaleDeliveryCtrHeader.setZtype("A");
        sapSaleDeliveryCtrHeader.setVstel(header.getShipPoint());
        sapSaleDeliveryCtrHeader.setLedat(ClientUtilDate.dateToSap(header.getDeliveryDeadline()));

        // 组装SAP行项目参数
        List<SapSaleDeliveryCtrItem> itemListSap = new ArrayList<>();
        for (SaleDeliveryItem item : itemList) {
            SapSaleDeliveryCtrItem sapSaleDeliveryCtrItem = new SapSaleDeliveryCtrItem();
            sapSaleDeliveryCtrItem.setRefDoc(item.getOrderNo());
            sapSaleDeliveryCtrItem.setRefItem(item.getOrderItemNo());
            sapSaleDeliveryCtrItem.setDlvQty(item.getDlvQty());
            sapSaleDeliveryCtrItem.setSalesUnit(item.getUnit());
            itemListSap.add(sapSaleDeliveryCtrItem);
        }

        // 组装SAP参数
        SapSaleDeliveryCtr sapSaleDeliveryCtr = new SapSaleDeliveryCtr();
        sapSaleDeliveryCtr.setHeader(sapSaleDeliveryCtrHeader);
        sapSaleDeliveryCtr.setItemList(itemListSap);

        // 调用SAP接口
        clientResult = sapSaleDeliveryCreateService.createSaleDelivery(sapSaleDeliveryCtr, deliveryId);
        clientResult.setStatus(clientResult.getStatus());

        return clientResult;
    }

    /**
     * 发货过账
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClientResult postGoods(SaleDeliveryPost postData) {
        ClientResult clientResult = new ClientResult();
        SaleDeliveryHeader header = saleDeliveryHeaderService.selectById(postData.getDeliveryId());
        if (header == null || header.getVbeln() == null) {
            return clientResult;
        }
        // 组装SAP抬头参数
        SapSaleDeliveryPostCtrHeader sapHeader = new SapSaleDeliveryPostCtrHeader();
        sapHeader.setIfDocno(header.getBussDocId());
        sapHeader.setVbelnVl(header.getVbeln());
        sapHeader.setKodat(ClientUtilDate.dateToSap(postData.getPickDate()));
        sapHeader.setWadatIst(ClientUtilDate.dateToSap(postData.getPostDate()));
        sapHeader.setWadat(ClientUtilDate.dateToSap(postData.getShipDate()));
        sapHeader.setWabuc("X");
        // 组装SAP请求
        SapSaleDeliveryPostCtr sapSaleDeliveryPostCtr = new SapSaleDeliveryPostCtr();
        sapSaleDeliveryPostCtr.setHeader(sapHeader);
        // 调用SAP，传入postData用于过账成功后保存日期+SAP返回值
        clientResult = sapSaleDeliveryPostService.postSaleDelivery(sapSaleDeliveryPostCtr, postData);
        return clientResult;
    }

    /**
     * 冲销发货过账
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClientResult reversePostGoods(String deliveryId) {
        ClientResult clientResult = new ClientResult();
        SaleDeliveryHeader header = saleDeliveryHeaderService.selectById(deliveryId);
        if (header == null || header.getVbeln() == null) {
            return clientResult;
        }
        SaleDeliveryPost post = saleDeliveryPostService.selectById(deliveryId);
        if (post == null || post.getMblnr() == null) {
            return clientResult;
        }
        // 组装SAP抬头参数（冲销）
        SapSaleDeliveryPostCtrHeader sapHeader = new SapSaleDeliveryPostCtrHeader();
        sapHeader.setIfDocno(header.getBussDocId());
        sapHeader.setVbelnVl(header.getVbeln());
        sapHeader.setKodat(ClientUtilDate.dateToSap(post.getPickDate()));
        sapHeader.setWadatIst(ClientUtilDate.dateToSap(post.getPostDate()));
        sapHeader.setWadat(ClientUtilDate.dateToSap(post.getShipDate()));
        sapHeader.setWabuc("C");
        // 组装SAP请求
        SapSaleDeliveryPostCtr sapSaleDeliveryPostCtr = new SapSaleDeliveryPostCtr();
        sapSaleDeliveryPostCtr.setHeader(sapHeader);
        // 调用SAP
        clientResult = sapSaleDeliveryPostService.reversePostSaleDelivery(sapSaleDeliveryPostCtr, deliveryId);
        return clientResult;
    }
}

package com.szd.z000.web.sap.mm.purStore.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientResultMsgBox;
import com.szd.core.client.util.ClientUtilDate;
import com.szd.z000.base.orm.domain.mm.purStore.PurStoreHeader;
import com.szd.z000.base.orm.domain.mm.purStore.PurStoreItem;
import com.szd.z000.common.sap.domain.purStore.*;
import com.szd.z000.common.sap.domain.service.SapPurStoreCreateService;
import com.szd.z000.common.sap.domain.service.SapPurStoreReverseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import com.szd.z000.web.sap.mm.purStore.enums.PurStoreBstpEnum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 物料凭证-SAP服务
 */
@Service
public class PurStoreSapService {
    private static final Logger logger = LoggerFactory.getLogger(PurStoreSapService.class);

    @Resource
    private PurStoreHeaderService purStoreHeaderService;
    @Resource
    private PurStoreItemService purStoreItemService;
    @Resource
    private SapPurStoreCreateService sapPurStoreCreateService;
    @Resource
    private SapPurStoreReverseService sapPurStoreReverseService;
    @Resource
    private ClientCore clientCore;

    /**
     * 推送SAP创建入库
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClientResult genSapPurStore(String storeId) {
        ClientResult clientResult = new ClientResult();
        PurStoreHeader header = purStoreHeaderService.selectById(storeId);
        if (header == null || !"D".equals(header.getBussStatus())) {
            return clientResult;
        }
        List<PurStoreItem> itemList = purStoreItemService.selectList(storeId);

        // 根据业务类型匹配枚举，获取对应的SAP事务代码和移动类型
        String bstp = header.getBstp();
        PurStoreBstpEnum bstpEnum = PurStoreBstpEnum.getByBstp(bstp);
        String gmCode = bstpEnum.getGmCode();
        String moveType = bstpEnum.getMoveType();

        // 封装SAP参数-抬头
        SapPurStoreCtrHeader sapPurStoreCtrHeader = new SapPurStoreCtrHeader();
        sapPurStoreCtrHeader.setIfDocno(header.getBussDocId());
        sapPurStoreCtrHeader.setPstngDate(ClientUtilDate.dateToSap(header.getPostDate()));
        sapPurStoreCtrHeader.setDocDate(ClientUtilDate.dateToSap(header.getVchDate()));
        sapPurStoreCtrHeader.setHeaderTxt(header.getTitle());
        sapPurStoreCtrHeader.setGmCode(gmCode);

        // 封装SAP参数-行项目
        List<SapPurStoreCtrItem> itemListSap = new ArrayList<>();
        for (PurStoreItem item : itemList) {
            SapPurStoreCtrItem sapPurStoreCtrItem = new SapPurStoreCtrItem();
            sapPurStoreCtrItem.setMaterial(item.getMtlCode());
            sapPurStoreCtrItem.setPlant(item.getFact());
            sapPurStoreCtrItem.setStgeLoc(item.getStore());
            sapPurStoreCtrItem.setMoveType(moveType);
            sapPurStoreCtrItem.setEntryQnt(item.getQuantity());
            sapPurStoreCtrItem.setEntryUom(item.getUnit());
            if ("Z000_MM_CGRK01".equals(bstp)) {
                sapPurStoreCtrItem.setPoNumber(item.getOrderNo());
                sapPurStoreCtrItem.setPoItem(item.getOrderItemNo());
            } else if ("Z000_MM_CGRK02".equals(bstp)) {
                sapPurStoreCtrItem.setOrderNo(item.getOrderNo());
            } else if ("Z000_MM_CGRK03".equals(bstp)) {
                sapPurStoreCtrItem.setOrderNo(item.getOrderNo());
            }
            sapPurStoreCtrItem.setItemText(item.getItemText());
            itemListSap.add(sapPurStoreCtrItem);
        }

        // 组装SAP参数
        SapPurStoreCtr sapPurStoreCtr = new SapPurStoreCtr();
        sapPurStoreCtr.setHeader(sapPurStoreCtrHeader);
        sapPurStoreCtr.setItemList(itemListSap);

        // 调用SAP接口创建入库
        clientResult = sapPurStoreCreateService.createPurStore(sapPurStoreCtr, storeId);
        clientResult.setStatus(clientResult.getStatus());

        return clientResult;
    }

    /***
     * 入库-SAP冲销（独立冲销按钮调用）
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClientResult reverseSapPurStore(String storeId, Date budat) {
        ClientResult clientResult = new ClientResult();
        PurStoreHeader header = purStoreHeaderService.selectObjById(storeId);
        if (header == null || header.getMblnr() == null || header.getMblnr().isEmpty()) {
            clientResult.setStatus("E");
            clientResult.setMsg("未找到物料凭证，无法冲销");
            ClientResultMsgBox msgBox = new ClientResultMsgBox("E", "未找到物料凭证，无法冲销");
            clientResult.getRetMsgBox().add(msgBox);
            return clientResult;
        }
        // 封装SAP冲销参数
        SapPurStoreReverseCtr sapPurStoreReverseCtr = new SapPurStoreReverseCtr();
        sapPurStoreReverseCtr.setMatDoc(header.getMblnr());
        sapPurStoreReverseCtr.setDocYear(header.getMjahr());
        sapPurStoreReverseCtr.setBudat(ClientUtilDate.dateToSap(budat));
        // 调用SAP接口冲销入库
        clientResult = sapPurStoreReverseService.reversePurStore(sapPurStoreReverseCtr, header.getStoreId());
        return clientResult;
    }
}

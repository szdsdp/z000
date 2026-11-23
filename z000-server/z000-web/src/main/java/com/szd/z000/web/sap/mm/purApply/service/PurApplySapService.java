package com.szd.z000.web.sap.mm.purApply.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.z000.base.orm.domain.mm.purApply.PurApplyHeader;
import com.szd.z000.base.orm.domain.mm.purApply.PurApplyItem;
import com.szd.z000.common.sap.domain.purApply.SapPurApplyCtr;
import com.szd.z000.common.sap.domain.purApply.SapPurApplyCtrHeader;
import com.szd.z000.common.sap.domain.purApply.SapPurApplyCtrItem;
import com.szd.z000.common.sap.domain.service.SapPurApplyCreateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 采购申请-SAP服务
 */
@Service
public class PurApplySapService {
    private static final Logger logger = LoggerFactory.getLogger(PurApplySapService.class);

    @Resource
    private PurApplyHeaderService purApplyHeaderService;
    @Resource
    private PurApplyItemService purApplyItemService;
    @Resource
    private SapPurApplyCreateService sapPurApplyCreateService;
    @Resource
    private ClientCore clientCore;

    /**
     * 推送SAP创建采购申请
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClientResult genSapPurApply(String applyId) {
        ClientResult clientResult = new ClientResult();
        PurApplyHeader header = purApplyHeaderService.selectById(applyId);
        if (header == null || !"D".equals(header.getBussStatus())) {
            return clientResult;
        }
        List<PurApplyItem> itemList = purApplyItemService.selectList(applyId);

        // 封装SAP参数-抬头
        SapPurApplyCtrHeader sapPurApplyCtrHeader = new SapPurApplyCtrHeader();
        sapPurApplyCtrHeader.setIfDocno(header.getBussDocId());
        sapPurApplyCtrHeader.setPrType(header.getApplyType());

        // 封装SAP参数-行项目
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        List<SapPurApplyCtrItem> itemListSap = new ArrayList<>();
        for (PurApplyItem item : itemList) {
            SapPurApplyCtrItem sapPurApplyCtrItem = new SapPurApplyCtrItem();
            sapPurApplyCtrItem.setPreqItem(String.format("%05d", item.getItemNo() * 10));
            sapPurApplyCtrItem.setPurGroup(item.getPurGroup());
            sapPurApplyCtrItem.setPreqName(item.getNeedUser());
            sapPurApplyCtrItem.setShortText(item.getItemText());
            sapPurApplyCtrItem.setMaterial(item.getMtlCode());
            sapPurApplyCtrItem.setPlant(item.getFact());
            sapPurApplyCtrItem.setStoreLoc(item.getStore());
            sapPurApplyCtrItem.setTrackingno(item.getNeedNo());
            sapPurApplyCtrItem.setMatlGroup(item.getMtlGroup());
            sapPurApplyCtrItem.setQuantity(item.getApplyNum());
            sapPurApplyCtrItem.setUnit(item.getUnit());
            if (item.getNeedDate() != null) {
                sapPurApplyCtrItem.setPreqDate(sdf.format(item.getNeedDate()));
            }
            if (item.getDeliveryDate() != null) {
                sapPurApplyCtrItem.setDelivDate(sdf.format(item.getDeliveryDate()));
            }
            itemListSap.add(sapPurApplyCtrItem);
        }

        // 组装SAP参数
        SapPurApplyCtr sapPurApplyCtr = new SapPurApplyCtr();
        sapPurApplyCtr.setHeader(sapPurApplyCtrHeader);
        sapPurApplyCtr.setItemList(itemListSap);

        // 调用SAP接口创建采购申请
        clientResult = sapPurApplyCreateService.createPurApply(sapPurApplyCtr, applyId);
        clientResult.setStatus(clientResult.getStatus());

        return clientResult;
    }
}

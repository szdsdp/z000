package com.szd.z000.web.sap.mm.purContract.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.util.ClientUtilDate;
import com.szd.z000.base.orm.domain.mm.purContract.PurContractHeader;
import com.szd.z000.base.orm.domain.mm.purContract.PurContractItem;
import com.szd.z000.common.sap.domain.purContract.SapPurContractCtr;
import com.szd.z000.common.sap.domain.purContract.SapPurContractCtrHeader;
import com.szd.z000.common.sap.domain.purContract.SapPurContractCtrItem;
import com.szd.z000.common.sap.domain.service.SapPurContractCreateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 采购合同-SAP服务
 */
@Service
public class PurContractSapService {
    private static final Logger logger = LoggerFactory.getLogger(PurContractSapService.class);

    @Resource
    private PurContractHeaderService purContractHeaderService;
    @Resource
    private PurContractItemService purContractItemService;
    @Resource
    private SapPurContractCreateService sapPurContractCreateService;
    @Resource
    private ClientCore clientCore;

    /**
     * 推送SAP创建采购合同
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClientResult genSapPurContract(String contractId) {
        ClientResult clientResult = new ClientResult();
        PurContractHeader header = purContractHeaderService.selectById(contractId);
        if (header == null || !"D".equals(header.getBussStatus())) {
            return clientResult;
        }
        List<PurContractItem> itemList = purContractItemService.selectList(contractId);

        // 封装SAP参数-抬头
        SapPurContractCtrHeader sapPurContractCtrHeader = new SapPurContractCtrHeader();
        sapPurContractCtrHeader.setIfDocno(header.getBussDocId());
        sapPurContractCtrHeader.setDocType(header.getType());
        sapPurContractCtrHeader.setVendor(header.getSupp());
        sapPurContractCtrHeader.setPurchOrg(header.getPurOrg());
        sapPurContractCtrHeader.setPurGroup(header.getPurGroup());
        sapPurContractCtrHeader.setCurrency(header.getCurr());
        sapPurContractCtrHeader.setVperStart(ClientUtilDate.dateToSap(header.getStartDate()));
        sapPurContractCtrHeader.setVperEnd(ClientUtilDate.dateToSap(header.getEndDate()));

        // 封装SAP参数-行项目
        List<SapPurContractCtrItem> itemListSap = new ArrayList<>();
        for (PurContractItem item : itemList) {
            SapPurContractCtrItem sapPurContractCtrItem = new SapPurContractCtrItem();
            sapPurContractCtrItem.setItemNo(item.getItemNo());
            sapPurContractCtrItem.setShortText(item.getItemText());
            sapPurContractCtrItem.setMaterial(item.getMtlCode());
            sapPurContractCtrItem.setPlant(item.getFact());
            sapPurContractCtrItem.setStgeLoc(item.getStore());
            sapPurContractCtrItem.setSupplStloc(item.getSendStore());
            sapPurContractCtrItem.setTrackingno(item.getNeedNo());
            sapPurContractCtrItem.setMatlGroup(item.getMtlGroup());
            sapPurContractCtrItem.setTargetQty(item.getQuantity());
            sapPurContractCtrItem.setPoUnit(item.getUnit());
            sapPurContractCtrItem.setNetPrice(item.getPrice());
            sapPurContractCtrItem.setPriceUnit(item.getPriceUnit());
            itemListSap.add(sapPurContractCtrItem);
        }

        // 组装SAP参数
        SapPurContractCtr sapPurContractCtr = new SapPurContractCtr();
        sapPurContractCtr.setHeader(sapPurContractCtrHeader);
        sapPurContractCtr.setItemList(itemListSap);

        // 调用SAP接口创建采购合同
        clientResult = sapPurContractCreateService.createPurContract(sapPurContractCtr, contractId);
        clientResult.setStatus(clientResult.getStatus());

        return clientResult;
    }
}

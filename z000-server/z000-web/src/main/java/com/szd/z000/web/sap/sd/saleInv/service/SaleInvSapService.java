package com.szd.z000.web.sap.sd.saleInv.service;

import com.szd.core.client.domain.common.ClientResult;
import com.szd.z000.base.orm.domain.sd.saleInv.SaleInvHeader;
import com.szd.z000.base.orm.domain.sd.saleInv.SaleInvItem;
import com.szd.z000.common.sap.domain.saleInv.SapSaleInvCtr;
import com.szd.z000.common.sap.domain.saleInv.SapSaleInvCtrHeader;
import com.szd.z000.common.sap.domain.saleInv.SapSaleInvCtrItem;
import com.szd.z000.common.sap.domain.service.SapSaleInvService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 销售开票-SAP服务
 */
@Service
public class SaleInvSapService {
    private static final Logger logger = LoggerFactory.getLogger(SaleInvSapService.class);

    @Resource
    private SaleInvHeaderService saleInvHeaderService;
    @Resource
    private SaleInvItemService saleInvItemService;
    @Resource
    private SapSaleInvService sapSaleInvService;

    /**
     * 出具发票
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClientResult issueInvoice(String invId) {
        ClientResult clientResult = new ClientResult();
        SaleInvHeader header = saleInvHeaderService.selectById(invId);
        if (header == null || !"D".equals(header.getBussStatus())) {
            return clientResult;
        }
        List<SaleInvItem> itemList = saleInvItemService.selectList(invId);

        // 组装SAP抬头参数
        SapSaleInvCtrHeader sapHeader = new SapSaleInvCtrHeader();
        sapHeader.setIfDocno(header.getBussDocId());
        sapHeader.setBudat(header.getBudat());
        sapHeader.setDocType(header.getDocType());
        sapHeader.setZtype("X");

        // 组装SAP行项目参数
        List<SapSaleInvCtrItem> sapItemList = new ArrayList<>();
        for (SaleInvItem item : itemList) {
            SapSaleInvCtrItem sapItem = new SapSaleInvCtrItem();
            sapItem.setVbelnVl(item.getVbelnVl());
            sapItemList.add(sapItem);
        }

        // 组装SAP参数
        SapSaleInvCtr sapCtr = new SapSaleInvCtr();
        sapCtr.setHeader(sapHeader);
        sapCtr.setItemList(sapItemList);

        // 调用SAP接口
        clientResult = sapSaleInvService.issueInvoice(sapCtr, header);

        return clientResult;
    }

    /**
     * 冲销出具发票
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClientResult cancelInvoice(String invId) {
        ClientResult clientResult = new ClientResult();
        SaleInvHeader header = saleInvHeaderService.selectById(invId);
        if (header == null || header.getBillDoc() == null) {
            return clientResult;
        }

        List<SaleInvItem> itemList = saleInvItemService.selectList(invId);

        // 组装SAP抬头参数（冲销）
        SapSaleInvCtrHeader sapHeader = new SapSaleInvCtrHeader();
        sapHeader.setIfDocno(header.getBussDocId());
        sapHeader.setBudat(header.getBudat());
        sapHeader.setDocType(header.getDocType());
        sapHeader.setZtype("C");
        sapHeader.setBillDoc(header.getBillDoc());

        // 组装SAP行项目参数
        List<SapSaleInvCtrItem> sapItemList = new ArrayList<>();
        for (SaleInvItem item : itemList) {
            SapSaleInvCtrItem sapItem = new SapSaleInvCtrItem();
            sapItem.setVbelnVl(item.getVbelnVl());
            sapItemList.add(sapItem);
        }

        // 组装SAP参数
        SapSaleInvCtr sapCtr = new SapSaleInvCtr();
        sapCtr.setHeader(sapHeader);
        sapCtr.setItemList(sapItemList);

        // 调用SAP接口
        clientResult = sapSaleInvService.cancelInvoice(sapCtr, invId);
        return clientResult;
    }
}

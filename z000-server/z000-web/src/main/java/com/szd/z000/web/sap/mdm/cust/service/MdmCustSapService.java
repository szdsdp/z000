package com.szd.z000.web.sap.mdm.cust.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.z000.base.orm.domain.mdm.cust.*;
import com.szd.z000.common.sap.domain.cust.*;
import com.szd.z000.common.sap.domain.service.SapCustCreateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 客商信息-sap服务
 */
@Service
public class MdmCustSapService {
    @Resource
    private MdmCustHeaderService mdmCustHeaderService;
    @Resource
    private MdmCustCmpyService mdmCustCmpyService;
    @Resource
    private MdmCustBkacService mdmCustBkacService;
    @Resource
    private MdmCustSaleService mdmCustSaleService;
    @Resource
    private MdmCustPurchaseService mdmCustPurchaseService;
    @Resource
    private MdmCustCreditService mdmCustCreditService;
    @Resource
    private SapCustCreateService sapCustCreateService;
    @Resource
    private ClientCore clientCore;

    /**
     * 更新SAP
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClientResult genSapCust(String applyId) {
        ClientResult clientResult = new ClientResult();
        MdmCustHeader header = mdmCustHeaderService.selectById(applyId);
        if ((header == null || !"D".equals(header.getBussStatus()))) {
            return clientResult;
        }
        // 推送SAP创建客商主数据
        List<MdmCustCmpy> cmpyList = mdmCustCmpyService.selectList(applyId);
        List<MdmCustBkac> bkacList = mdmCustBkacService.selectList(applyId);
        List<MdmCustSale> saleList = mdmCustSaleService.selectList(applyId);
        List<MdmCustPurchase> purchaseList = mdmCustPurchaseService.selectList(applyId);
        List<MdmCustCredit> creditList = mdmCustCreditService.selectList(applyId);

        // 封装SAP参数-抬头
        SapCustCtrHeader sapCustCtrHeader = new SapCustCtrHeader();
        sapCustCtrHeader.setCustId(header.getCustId());
        sapCustCtrHeader.setCustName(header.getCustName());
        sapCustCtrHeader.setU8Code(header.getU8Code());
        sapCustCtrHeader.setCustNameShort(header.getCustNameShort());
        sapCustCtrHeader.setPartnerType(header.getPartnerType());
        sapCustCtrHeader.setCustGrp(header.getCustGrp());
        sapCustCtrHeader.setCtry(header.getCtry());
        sapCustCtrHeader.setProvince(header.getProvince());
        sapCustCtrHeader.setCity(clientCore.mdm.getDescA("CORE_CITY", header.getCity(), "1"));
        sapCustCtrHeader.setAddress(header.getAddress());
        sapCustCtrHeader.setPhone(header.getPhone());
        sapCustCtrHeader.setContact(header.getContact());
        sapCustCtrHeader.setIdNum(header.getIdNum());
        //if (header.getCustFlagDB().contains("D")) {
        //    sapCustCtrHeader.setCustFlagD("X");
        //}
        //if (header.getCustFlagDB().contains("K")) {
        //    sapCustCtrHeader.setCustFlagK("X");
        //}
        sapCustCtrHeader.setRiskClass(header.getRiskClass());
        sapCustCtrHeader.setTaxClass(header.getTaxClass());
        sapCustCtrHeader.setPostCode(header.getPostCode());
        // 封装SAP参数-银行信息
        List<SapCustCtrItemBkac> bkacListSap = new ArrayList<>();
        for (MdmCustBkac bkac : bkacList) {
            SapCustCtrItemBkac sapCustCtrItemBkac = new SapCustCtrItemBkac();
            sapCustCtrItemBkac.setAcctNo(bkac.getAcctNo());
            sapCustCtrItemBkac.setAcctName(bkac.getAcctName());
            sapCustCtrItemBkac.setCnap(bkac.getCnap());
            sapCustCtrItemBkac.setCnapName(bkac.getCnapName());
            bkacListSap.add(sapCustCtrItemBkac);
        }
        // 封装SAP参数-公司信息
        List<SapCustCtrItemCmpy> cmpyKListSap = new ArrayList<>();
        List<SapCustCtrItemCmpy> cmpyDListSap = new ArrayList<>();
        for (MdmCustCmpy cmpy : cmpyList) {
            SapCustCtrItemCmpy sapCustCtrItemCmpy = new SapCustCtrItemCmpy();
            sapCustCtrItemCmpy.setCmpy(cmpy.getCmpy());
            sapCustCtrItemCmpy.setAccs(cmpy.getAccs());
            sapCustCtrItemCmpy.setPayTerms(cmpy.getPayTerms());
            sapCustCtrItemCmpy.setPayType(cmpy.getPayType());
            if ("K".equals(cmpy.getCustFlag())) {
                cmpyKListSap.add(sapCustCtrItemCmpy);
            }
            if ("D".equals(cmpy.getCustFlag())) {
                cmpyDListSap.add(sapCustCtrItemCmpy);
            }
        }
        // 封装SAP参数-销售组织信息
        List<SapCustCtrItemSale> saleListSap = new ArrayList<>();
        for (MdmCustSale sale : saleList) {
            SapCustCtrItemSale sapCustCtrItemSale = new SapCustCtrItemSale();
            sapCustCtrItemSale.setSaleOrg(sale.getSaleOrg());
            sapCustCtrItemSale.setVtweg(sale.getVtweg());
            sapCustCtrItemSale.setSpart(sale.getSpart());
            sapCustCtrItemSale.setCurr(sale.getCurr());
            sapCustCtrItemSale.setPriceTax(sale.getPriceTax());
            sapCustCtrItemSale.setCustClass(sale.getCustClass());
            sapCustCtrItemSale.setCustClassSub(sale.getCustClassSub());
            sapCustCtrItemSale.setPayTerms(sale.getPayTerms());
            sapCustCtrItemSale.setAccsGrp(sale.getAccsGrp());
            sapCustCtrItemSale.setIncoterms(sale.getIncoterms());
            sapCustCtrItemSale.setIncoterms1(sale.getIncoterms1());
            sapCustCtrItemSale.setSaleDept(sale.getSaleDept());
            sapCustCtrItemSale.setSaleRegion(sale.getSaleRegion());
            sapCustCtrItemSale.setShippingTerms(sale.getShippingTerms());
            sapCustCtrItemSale.setCreditCtrlArea(sale.getCreditCtrlArea());

            List<SapCustCtrItemSalePartner> salePartnerListSap = new ArrayList<>();
            for (MdmCustSalePartner partner : sale.getPartnerList()) {
                SapCustCtrItemSalePartner sapCustCtrItemSalePartner = new SapCustCtrItemSalePartner();
                sapCustCtrItemSalePartner.setPartnerType(partner.getPartnerType());
                sapCustCtrItemSalePartner.setPartnerCode(partner.getPartnerCode());
                salePartnerListSap.add(sapCustCtrItemSalePartner);
            }
            sapCustCtrItemSale.setPartnerList(salePartnerListSap);
            saleListSap.add(sapCustCtrItemSale);
        }
        // 封装SAP参数-采购组织信息
        List<SapCustCtrItemPurchase> purchaseListSap = new ArrayList<>();
        for (MdmCustPurchase purchase : purchaseList) {
            SapCustCtrItemPurchase sapCustCtrItemPurchase = new SapCustCtrItemPurchase();
            sapCustCtrItemPurchase.setPurchaseOrg(purchase.getPurchaseOrg());
            sapCustCtrItemPurchase.setCurr(purchase.getCurr());
            sapCustCtrItemPurchase.setPayTerms(purchase.getPayTerms());
            //sapCustCtrItemPurchase.setDealType(purchase.getDealType());
            sapCustCtrItemPurchase.setContact(purchase.getContact());
            sapCustCtrItemPurchase.setContactPhone(purchase.getContactPhone());
            purchaseListSap.add(sapCustCtrItemPurchase);
        }
        // 封装SAP参数-信用信息
        List<SapCustCtrItemCredit> creditListSap = new ArrayList<>();
        for (MdmCustCredit credit : creditList) {
            SapCustCtrItemCredit sapCustCtrItemCredit = new SapCustCtrItemCredit();
            sapCustCtrItemCredit.setCreditSgmnt(credit.getCreditSgmnt());
            sapCustCtrItemCredit.setCreditLimit(credit.getCreditLimit());
            creditListSap.add(sapCustCtrItemCredit);
        }
        SapCustCtr sapCustCtr = new SapCustCtr();
        sapCustCtr.setHeader(sapCustCtrHeader);
        sapCustCtr.setBkacList(bkacListSap);
        sapCustCtr.setCmpyKList(cmpyKListSap);
        sapCustCtr.setCmpyDList(cmpyDListSap);
        sapCustCtr.setSaleList(saleListSap);
        sapCustCtr.setPurchaseList(purchaseListSap);
        sapCustCtr.setCreditList(creditListSap);
        // 调用SAP接口
        clientResult = sapCustCreateService.createCust(sapCustCtr, applyId);
        clientResult.setStatus(clientResult.getStatus());

        return clientResult;
    }

    public ClientResult checkSapCust(MdmCustHeader header) {
        ClientResult clientResult = new ClientResult();
        SapCustCheckCtr sapCustCheckCtr = new SapCustCheckCtr();
        sapCustCheckCtr.setCustId(header.getCustId());
        sapCustCheckCtr.setCustName(header.getCustName());
        sapCustCheckCtr.setIdNum(header.getIdNum());
        // 调用SAP接口
        clientResult = sapCustCreateService.checkCust(sapCustCheckCtr);


        return clientResult;
    }
}

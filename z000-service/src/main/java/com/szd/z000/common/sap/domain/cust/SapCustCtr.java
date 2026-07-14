package com.szd.z000.common.sap.domain.cust;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 客商创建
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapCustCtr {
    /**
     * 抬头
     */
    @XmlElement(name = "HEADER")
    private SapCustCtrHeader header;
    /**
     * 银行信息
     */
    @XmlElementWrapper(name = "ITEM_BANK")
    @XmlElement(name = "item")
    private List<SapCustCtrItemBkac> bkacList;
    /**
     * 客户-公司信息-客户
     */
    @XmlElementWrapper(name = "ITEM_KNB1")
    @XmlElement(name = "item")
    private List<SapCustCtrItemCmpy> cmpyKList;

    /**
     * 客户-销售组织信息
     */
    @XmlElementWrapper(name = "ITEM_KNVV")
    @XmlElement(name = "item")
    private List<SapCustCtrItemSale> saleList;

    /**
     * 客户-信用信息
     */
    @XmlElementWrapper(name = "ITEM_CSGM")
    @XmlElement(name = "item")
    private List<SapCustCtrItemCredit> creditList;

    /**
     * 供应商-公司信息-供应商
     */
    @XmlElementWrapper(name = "ITEM_LFB1")
    @XmlElement(name = "item")
    private List<SapCustCtrItemCmpy> cmpyDList;

    /**
     * 供应商-采购信息
     */
    @XmlElementWrapper(name = "ITEM_LFM1")
    @XmlElement(name = "item")
    private List<SapCustCtrItemPurchase> purchaseList;
}

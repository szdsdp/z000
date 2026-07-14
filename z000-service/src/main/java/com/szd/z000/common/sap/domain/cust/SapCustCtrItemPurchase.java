package com.szd.z000.common.sap.domain.cust;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 客商维护-采购组织信息
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
@Data
public class SapCustCtrItemPurchase {

    /**
     * 采购组织
     */
    @XmlElement(name = "EKORG")
    private String purchaseOrg;

    /**
     * 货币
     */
    @XmlElement(name = "WAERS")
    private String curr;

    /**
     * 付款条件代码
     */
    @XmlElement(name = "ZTERM")
    private String payTerms;
    /**
     * 国际贸易条款（成交方式）
     */
    @XmlElement(name = "INCO1")
    private String dealType;

    /**
     * 供应商联系人
     */
    @XmlElement(name = "VERKF")
    private String contact;

    /**
     * 联系人电话
     */
    @XmlElement(name = "TELF1")
    private String contactPhone;
}

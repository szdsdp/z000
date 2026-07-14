package com.szd.z000.common.sap.domain.cust;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 客商维护-销售组织-合作伙伴信息
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
@Data
public class SapCustCtrItemSalePartner {

    /**
     * 合作伙伴职能
     */
    @XmlElement(name = "PARVW")
    private String partnerType;

    /**
     * 业务伙伴编号
     */
    @XmlElement(name = "PARTNER")
    private String partnerCode;
}

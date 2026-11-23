package com.szd.z000.common.sap.domain.saleOrder;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 销售订单创建-抬头
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "HEADER")
@Data
public class SapSaleOrderCtrHeader {
    /**
     * 单据号[外]
     */
    @XmlElement(name = "IF_DOCNO")
    private String ifDocno;
    /**
     * 售达方
     */
    @XmlElement(name = "KUNNR_AG")
    private String kunnrAg;
    /**
     * 送达方
     */
    @XmlElement(name = "KUNNR_WE")
    private String kunnrWe;
    /**
     * 开票方
     */
    @XmlElement(name = "KUNNR_RE")
    private String kunnrRe;
    /**
     * 订单类型
     */
    @XmlElement(name = "DOC_TYPE")
    private String docType;
    /**
     * 销售机构
     */
    @XmlElement(name = "SALES_ORG")
    private String salesOrg;
    /**
     * 分销渠道
     */
    @XmlElement(name = "DISTR_CHAN")
    private String distrChan;
    /**
     * 产品组
     */
    @XmlElement(name = "DIVISION")
    private String division;
}

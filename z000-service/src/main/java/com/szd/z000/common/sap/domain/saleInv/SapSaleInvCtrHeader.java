package com.szd.z000.common.sap.domain.saleInv;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 销售开票-抬头
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "HEADER")
@Data
public class SapSaleInvCtrHeader {
    /**
     * 单据号[外]
     */
    @XmlElement(name = "IF_DOCNO")
    private String ifDocno;
    /**
     * 过账日期
     */
    @XmlElement(name = "BUDAT")
    private String budat;
    /**
     * 发票类型
     */
    @XmlElement(name = "DOC_TYPE")
    private String docType;
    /**
     * 操作类型(X=出具发票/C=冲销出具发票)
     */
    @XmlElement(name = "ZTYPE")
    private String ztype;
    /**
     * 发票号
     */
    @XmlElement(name = "BILL_DOC")
    private String billDoc;
}

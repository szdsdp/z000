package com.szd.z000.common.sap.domain.purOrder;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 采购订单创建-抬头
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "HEADER")
@Data
public class SapPurOrderCtrHeader {
    /** 单据号[外] */
    @XmlElement(name = "IF_DOCNO")
    private String ifDocno;
    /** 采购凭证号 */
    @XmlElement(name = "PO_NUMBER")
    private String poNumber;
    /** 公司代码 */
    @XmlElement(name = "COMP_CODE")
    private String compCode;
    /** 采购凭证类型 */
    @XmlElement(name = "DOC_TYPE")
    private String docType;
    /** 供应商帐户号 */
    @XmlElement(name = "VENDOR")
    private String vendor;
    /** 采购组织 */
    @XmlElement(name = "PURCH_ORG")
    private String purchOrg;
    /** 采购组 */
    @XmlElement(name = "PUR_GROUP")
    private String purGroup;
    /** 货币码 */
    @XmlElement(name = "CURRENCY")
    private String currency;
    /** 采购凭证日期 */
    @XmlElement(name = "DOC_DATE")
    private String ebdat;
}

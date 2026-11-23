package com.szd.z000.common.sap.domain.purContract;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 采购合同创建-抬头
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "HEADER")
@Data
public class SapPurContractCtrHeader {
    /** 单据号[外] */
    @XmlElement(name = "IF_DOCNO")
    private String ifDocno;
    /** 类型 */
    @XmlElement(name = "DOC_TYPE")
    private String docType;
    /** 供应商 */
    @XmlElement(name = "VENDOR")
    private String vendor;
    /** 采购组织 */
    @XmlElement(name = "PURCH_ORG")
    private String purchOrg;
    /** 采购组 */
    @XmlElement(name = "PUR_GROUP")
    private String purGroup;
    /** 货币 */
    @XmlElement(name = "CURRENCY")
    private String currency;
    /** 生效日期 */
    @XmlElement(name = "VPER_START")
    private String vperStart;
    /** 失效日期 */
    @XmlElement(name = "VPER_END")
    private String vperEnd;
}

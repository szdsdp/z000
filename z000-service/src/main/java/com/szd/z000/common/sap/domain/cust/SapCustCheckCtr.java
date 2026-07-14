package com.szd.z000.common.sap.domain.cust;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 客商重复检查
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapCustCheckCtr {
    /** 客商编码 */
    @XmlElement(name = "PARTNER")
    private String custId;
    /** 客商名称 */
    @XmlElement(name = "NAME1")
    private String custName;
    /** 税号 */
    @XmlElement(name = "STCEG")
    private String idNum;
}

package com.szd.z000.common.sap.domain.cust;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 客商维护-公司信息
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
@Data
public class SapCustCtrItemCmpy {
    /**
     * 所属公司
     */
    @XmlElement(name = "BUKRS")
    private String cmpy;
    /**
     * 统驭科目
     */
    @XmlElement(name = "AKONT")
    private String accs;
    /**
     * 付款条件
     */
    @XmlElement(name = "ZTERM")
    private String payTerms;
    /**
     * 付款方式
     */
    @XmlElement(name = "ZWELS")
    private String payType;
}

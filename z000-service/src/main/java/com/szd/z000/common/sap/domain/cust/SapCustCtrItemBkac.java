package com.szd.z000.common.sap.domain.cust;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 客商维护-银行信息
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
@Data
public class SapCustCtrItemBkac {
    /** 银行账号 */
    @XmlElement(name = "ZYHZH")
    private String acctNo;
    /** 账户名称 */
    @XmlElement(name = "ZYHZH_T")
    private String acctName;
    /** 联行号  */
    @XmlElement(name = "CNAPS")
    private String cnap;
    /** 联行名   */
    @XmlElement(name = "CNAPS_T")
    private String cnapName;
    /** 国家 */
    @XmlElement(name = "LAND1")
    private String ctry;
}

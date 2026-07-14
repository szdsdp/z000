package com.szd.z000.common.sap.domain.cust;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

/**
 * 客商维护-客户-信用信息
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
@Data
public class SapCustCtrItemCredit {
    /** 信用段  */
    @XmlElement(name = "CREDIT_SGMNT")
    private String creditSgmnt;
    /** 信用额度 */
    @XmlElement(name = "CREDIT_LIMIT")
    private BigDecimal creditLimit;
}

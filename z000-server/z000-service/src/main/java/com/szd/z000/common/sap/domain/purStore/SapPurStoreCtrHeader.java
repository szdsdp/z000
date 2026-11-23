package com.szd.z000.common.sap.domain.purStore;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 物料凭证创建-抬头
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "HEADER")
@Data
public class SapPurStoreCtrHeader {
    /** 单据号[外] */
    @XmlElement(name = "IF_DOCNO")
    private String ifDocno;
    /** 凭证中的过帐日期 */
    @XmlElement(name = "PSTNG_DATE")
    private String pstngDate;
    /** 凭证中的凭证日期 */
    @XmlElement(name = "DOC_DATE")
    private String docDate;
    /** 凭证抬头文本 */
    @XmlElement(name = "HEADER_TXT")
    private String headerTxt;
    /** 为 BAPI 货物移动分配事务代码 */
    @XmlElement(name = "GM_CODE")
    private String gmCode;
}

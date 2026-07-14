package com.szd.z000.common.sap.domain.check.checkPlan;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 库存盘点创建-抬头
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "HEADER")
@Data
public class SapCheckPlanCtrHeader {
    /**
     * 单据号[外]
     */
    @XmlElement(name = "IF_DOCNO")
    private String ifDocno;
    /**
     * 凭证过帐日期
     */
    @XmlElement(name = "PSTNG_DATE")
    private String pstngDate;
    /**
     * 凭证日期
     */
    @XmlElement(name = "DOC_DATE")
    private String docDate;
    /**
     * 凭证抬头文本
     */
    @XmlElement(name = "HEADER_TXT")
    private String headerTxt;
    /**
     * 事务代码(03-盘点)
     */
    @XmlElement(name = "GM_CODE")
    private String gmCode;
}

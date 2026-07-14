package com.szd.z000.common.sap.domain.invCheck;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 发票校验-抬头(请求)
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapInvCheckCtrHeader {
    /** 单据号[外] */
    @XmlElement(name = "IF_DOCNO")
    private String ifDocno;
    /** 冲销标示[外] */
    @XmlElement(name = "IF_RFLAG")
    private String ifRflag;
    /** 记帐发票 */
    @XmlElement(name = "INVOICE_IND")
    private String invoiceInd;
    /** 凭证类型 */
    @XmlElement(name = "DOC_TYPE")
    private String docType;
    /** 凭证日期 */
    @XmlElement(name = "DOC_DATE")
    private String docDate;
    /** 过帐日期 */
    @XmlElement(name = "PSTNG_DATE")
    private String pstngDate;
    /** 公司代码 */
    @XmlElement(name = "COMP_CODE")
    private String compCode;
    /** 货币码 */
    @XmlElement(name = "CURRENCY")
    private String currency;
    /** 总发票金额 */
    @XmlElement(name = "GROSS_AMOUNT")
    private String grossAmount;
    /** 付款条件 */
    @XmlElement(name = "PMNTTRMS")
    private String pmnttrms;
    /** 基准日期 */
    @XmlElement(name = "BLINE_DATE")
    private String blineDate;
    /** 凭证抬头文本 */
    @XmlElement(name = "HEADER_TXT")
    private String headerTxt;
    /** 付款方式 */
    @XmlElement(name = "PYMT_METH")
    private String pymtMeth;
}

package com.szd.z000.common.sap.domain.payRece;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * SAP回款认领单创建-抬头
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class SapPayReceiptCtrHeader {
    // ==================== HEADER（抬头）通用字段 ====================
    /** 单据号[外] */
    @XmlElement(name = "IF_DOCNO")
    private String ifDocno;
    /** C创建/R冲销 */
    @XmlElement(name = "ZTYPE")
    private String ztype;
    /** 公司代码 */
    @XmlElement(name = "BUKRS")
    private String bukrs;
    /** 货币码 */
    @XmlElement(name = "WAERS")
    private String waers;
    /** 凭证类型 */
    @XmlElement(name = "BLART")
    private String blart;
    /** 凭证中的过帐日期 */
    @XmlElement(name = "BUDAT")
    private String budat;
    /** 凭证抬头文本 */
    @XmlElement(name = "BKTXT")
    private String bktxt;

    // ==================== HEADER（收款电汇）字段 ====================
    /** 总账科目 */
    @XmlElement(name = "HKONT")
    private String hkont;
    /** 项目文本 */
    @XmlElement(name = "SGTXT")
    private String sgtxt;
    /** 分配编号 */
    @XmlElement(name = "ZUONR")
    private String zuonr;
    /** 凭证货币金额 */
    @XmlElement(name = "WRBTR")
    private String wrbtr;

    // ==================== HEADER（收款票据信息）预留字段 ====================
    /** 凭证货币金额（票据） */
    @XmlElement(name = "A_WRBTR")
    private String aWrbtr;
    /** 业务范围（票据） */
    @XmlElement(name = "A_GSBER")
    private String aGsber;
    /** 利润中心（票据） */
    @XmlElement(name = "A_PRCTR")
    private String aPrctr;
    /** 应用程序段（票据） */
    @XmlElement(name = "A_SEGMENT")
    private String aSegment;
    /** 客户编号（票据） */
    @XmlElement(name = "A_KUNNR")
    private String aKunnr;
    /** 特别总账标识（票据） */
    @XmlElement(name = "A_UMSKZ")
    private String aUmskz;
    /** 到期日期（票据） */
    @XmlElement(name = "A_ZFBDT")
    private String aZfbdt;
    /** 出票人（票据） */
    @XmlElement(name = "A_WNAME")
    private String aWname;
    /** 收票人（票据） */
    @XmlElement(name = "A_WBZOG")
    private String aWbzog;
    /** 签发日期（票据） */
    @XmlElement(name = "A_WDATE")
    private String aWdate;
    /** 总账科目（票据） */
    @XmlElement(name = "A_HKONT")
    private String aHkont;
    /** 付款原因代码（票据） */
    @XmlElement(name = "A_RSTGR")
    private String aRstgr;
    /** 项目文本（票据） */
    @XmlElement(name = "A_SGTXT")
    private String aSgtxt;
    /** 分配编号（票据） */
    @XmlElement(name = "A_ZUONR")
    private String aZuonr;
    /** 合同号（票据） */
    @XmlElement(name = "A_VERTN")
    private String aVertn;
}

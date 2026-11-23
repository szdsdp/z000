package com.szd.z000.common.sap.domain.custRecon;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 客商对账查询-返回行项目
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapCustReconCtrRetItem {
    /** 公司代码 */
    @XmlElement(name = "BUKRS")
    private String bukrs;
    /** 客户 */
    @XmlElement(name = "KUNNR")
    private String kunnr;
    /** 客户描述 */
    @XmlElement(name = "KUNNR_TXT")
    private String kunnrTxt;
    /** 会计凭证 */
    @XmlElement(name = "BELNR_FI")
    private String belnrFi;
    /** 发票凭证 */
    @XmlElement(name = "VBELN")
    private String vbeln;
    /** 凭证类型 */
    @XmlElement(name = "BLART_FI")
    private String blartFi;
    /** 开票类型 */
    @XmlElement(name = "FKART")
    private String fkart;
    /** 过账日期 */
    @XmlElement(name = "BUDAT_FI")
    private String budatFi;
    /** 发票日期 */
    @XmlElement(name = "FKDAT")
    private String fkdat;
    /** 货币 */
    @XmlElement(name = "WAERS")
    private String waers;
    /** 原币金额(含税) */
    @XmlElement(name = "DMBTR_Y")
    private String dmbtrY;
    /** 本币金额(含税) */
    @XmlElement(name = "DMBTR_L")
    private String dmbtrL;
    /** 发票金额(含税) */
    @XmlElement(name = "DMBTR_SD")
    private String dmbtrSd;
    /** 财务金额(含税) */
    @XmlElement(name = "DMBTR_FI")
    private String dmbtrFi;
    /** 收入 */
    @XmlElement(name = "DMBTR")
    private String dmbtr;
    /** 余额 */
    @XmlElement(name = "DMBTR_YE")
    private String dmbtrYe;
    /** 抬头文本 */
    @XmlElement(name = "BKTXT")
    private String bktxt;
    /** 销售部 */
    @XmlElement(name = "BZIRK")
    private String bzirk;
    /** 产品组 */
    @XmlElement(name = "SPART")
    private String spart;
    /** 年度 */
    @XmlElement(name = "GJAHR_FI")
    private String gjahrFi;
    /** 参照 */
    @XmlElement(name = "XBLNR_FI")
    private String xblnrFi;
    /** 参考码 */
    @XmlElement(name = "AWKEY_FI")
    private String awkeyFi;
    /** 冲销凭证 */
    @XmlElement(name = "STBLG_FI")
    private String stblgFi;
    /** 冲销年度 */
    @XmlElement(name = "STJAH_FI")
    private String stjahFi;
    /** 汇率 */
    @XmlElement(name = "KURSF")
    private String kursf;
    /** 行项目 */
    @XmlElement(name = "BUZEI")
    private String buzei;
    /** 发票冲销标识 */
    @XmlElement(name = "FKSTO")
    private String fksto;
    /** 被冲销发票凭证 */
    @XmlElement(name = "SFAKN")
    private String sfakn;
}

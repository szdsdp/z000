package com.szd.z000.common.sap.domain.payClear;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 客商未清项抽取-返回行项目
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapPayClearCtrRetItem {
    /** 公司代码 */
    @XmlElement(name = "BUKRS")
    private String bukrs;
    /** 会计凭证编号 */
    @XmlElement(name = "BELNR")
    private String belnr;
    /** 财年 */
    @XmlElement(name = "GJAHR")
    private String gjahr;
    /** 凭证类型 */
    @XmlElement(name = "BLART")
    private String blart;
    /** 凭证抬头文本 */
    @XmlElement(name = "BKTXT")
    private String bktxt;
    /** 货币码 */
    @XmlElement(name = "WAERS")
    private String waers;
    /** 会计凭证中的行项目数 */
    @XmlElement(name = "BUZEI")
    private String buzei;
    /** 记帐代码 */
    @XmlElement(name = "BSCHL")
    private String bschl;
    /** 借方/贷方标识 */
    @XmlElement(name = "SHKZG")
    private String shkzg;
    /** 凭证货币金额 */
    @XmlElement(name = "WRBTR")
    private String wrbtr;
    /** 供应商或债权人的帐号 */
    @XmlElement(name = "LIFNR")
    private String lifnr;
    /** 名称（供应商） */
    @XmlElement(name = "LIFNR_T")
    private String lifnrT;
    /** 客户编号 */
    @XmlElement(name = "KUNNR")
    private String kunnr;
    /** 名称（客户） */
    @XmlElement(name = "KUNNR_T")
    private String kunnrT;
    /** 总账科目 */
    @XmlElement(name = "HKONT")
    private String hkont;
    /** 总帐科目成文本 */
    @XmlElement(name = "HKONT_T")
    private String hkontT;
    /** 分配编号 */
    @XmlElement(name = "ZUONR")
    private String zuonr;
    /** 项目文本 */
    @XmlElement(name = "SGTXT")
    private String sgtxt;
}

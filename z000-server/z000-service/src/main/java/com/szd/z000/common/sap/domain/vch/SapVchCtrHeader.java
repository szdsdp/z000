package com.szd.z000.common.sap.domain.vch;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * SAP会计凭证生成-抬头
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class SapVchCtrHeader {
    /** 单据号[外] */
    @XmlElement(name = "IF_DOCNO")
    private String ifDocno;
    /** 公司代码 */
    @XmlElement(name = "BUKRS")
    private String bukrs;
    /** 凭证类型 */
    @XmlElement(name = "BLART")
    private String blart;
    /** 凭证抬头文本 */
    @XmlElement(name = "BKTXT")
    private String bktxt;
    /** 货币码 */
    @XmlElement(name = "WAERS")
    private String waers;
    /** 汇率 */
    @XmlElement(name = "KURSF")
    private String kursf;
    /** 参考凭证编号 */
    @XmlElement(name = "XBLNR")
    private String xblnr;
    /** 凭证中的过帐日期 */
    @XmlElement(name = "BUDAT")
    private String budat;
    /** 凭证中的凭证日期 */
    @XmlElement(name = "BLDAT")
    private String bldat;
    /** 用户名 */
    @XmlElement(name = "USNAM")
    private String usnam;
    /** 发票的页数 */
    @XmlElement(name = "NUMPG")
    private String numpg;
}

package com.szd.z000.common.sap.domain.invCheck;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 发票校验-返回
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapInvCheckCtrRet {
    /** 单据号[外] */
    @XmlElement(name = "IF_DOCNO")
    private String ifDocno;
    /** 发票凭证编号 */
    @XmlElement(name = "RE_BELNR")
    private String reBelnr;
    /** 财年 */
    @XmlElement(name = "RE_GJAHR")
    private String reGjahr;
    /** 公司代码 */
    @XmlElement(name = "BUKRS")
    private String bukrs;
    /** 会计凭证编号 */
    @XmlElement(name = "BELNR")
    private String belnr;
    /** 财年 */
    @XmlElement(name = "GJAHR")
    private String gjahr;
    /** 过帐日期 */
    @XmlElement(name = "BUDAT")
    private String budat;
    /** 冲销凭证编号 */
    @XmlElement(name = "RE_BELNR_CX")
    private String reBelnrCx;
    /** 可逆凭证会计年度 */
    @XmlElement(name = "RE_GJAHR_CX")
    private String reGjahrCx;
    /** 会计凭证编号(冲销) */
    @XmlElement(name = "BELNR_QZ")
    private String belnrQz;
    /** 财年(冲销) */
    @XmlElement(name = "GJAHR_QZ")
    private String gjahrQz;
    /** 状态(S/E) */
    @XmlElement(name = "ZSTAU")
    private String zstau;
    /** 错误信息 */
    @XmlElement(name = "ZMESS")
    private String zmess;
}

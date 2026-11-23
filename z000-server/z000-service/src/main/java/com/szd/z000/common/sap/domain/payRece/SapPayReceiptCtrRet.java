package com.szd.z000.common.sap.domain.payRece;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * SAP回款认领单创建-返回
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapPayReceiptCtrRet {
    /** 凭证号 */
    @XmlElement(name = "BELNR")
    private String belnr;
    /** 年度 */
    @XmlElement(name = "GJAHR")
    private String gjahr;
    /** 公司代码 */
    @XmlElement(name = "BUKRS")
    private String bukrs;
    /** 状态（S成功 E错误） */
    @XmlElement(name = "ZSTAU")
    private String zstau;
    /** 错误信息 */
    @XmlElement(name = "ZMESS")
    private String zmess;
}

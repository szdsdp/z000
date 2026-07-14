package com.szd.z000.common.sap.domain.vch;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * SAP会计凭证生成-返回
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapVchCtrRet {
    /**
     * 公司代码
     */
    @XmlElement(name = "BUKRS")
    private String bukrs;
    /**
     * 会计凭证编号
     */
    @XmlElement(name = "BELNR")
    private String belnr;
    /**
     * 财年
     */
    @XmlElement(name = "GJAHR")
    private String gjahr;
    /**
     * 状态(S 成功,E 错误)
     */
    @XmlElement(name = "ZSTAU")
    private String zstau;
    /**
     * 错误信息
     */
    @XmlElement(name = "ZMESS")
    private String zmess;
}

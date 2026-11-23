package com.szd.z000.common.sap.domain.cust;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 客商维护-结果
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapCustCtrRet {

    /**
     * 客商编码
     */
    @XmlElement(name = "PARTNER")
    private String custId;

    /**
     * 状态(S 成功,E 错误)
     */
    @XmlElement(name = "ZSTAU")
    private String zstau;
    /**
     * 消息
     */
    @XmlElement(name = "ZMESS")
    private String zmess;
}

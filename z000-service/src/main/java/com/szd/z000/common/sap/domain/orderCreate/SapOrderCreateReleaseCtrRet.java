package com.szd.z000.common.sap.domain.orderCreate;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 生产订单下达-结果
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapOrderCreateReleaseCtrRet {
    /** 状态(S 成功,E 错误) */
    @XmlElement(name = "ZSTAU")
    private String zstau;
    /** 错误信息 */
    @XmlElement(name = "ZMESS")
    private String zmess;
}

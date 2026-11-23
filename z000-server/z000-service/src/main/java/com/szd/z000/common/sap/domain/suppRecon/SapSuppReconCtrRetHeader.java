package com.szd.z000.common.sap.domain.suppRecon;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 供应商对账查询-返回抬头
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "HEADER")
@Data
public class SapSuppReconCtrRetHeader {
    /** 状态(S 成功,E 错误) */
    @XmlElement(name = "ZSTAU")
    private String zstau;
    /** 消息 */
    @XmlElement(name = "ZMESS")
    private String zmess;
}

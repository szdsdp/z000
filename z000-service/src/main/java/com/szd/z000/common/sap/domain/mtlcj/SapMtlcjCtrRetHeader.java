package com.szd.z000.common.sap.domain.mtlcj;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 物料主数据创建/修改-返回抬头
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "HEADER")
@Data
public class SapMtlcjCtrRetHeader {
    /** 消息类型 */
    @XmlElement(name = "ZSTAU")
    private String zstau;
    /** 消息文本 */
    @XmlElement(name = "ZMESS")
    private String zmess;
    /** 物料号 */
    @XmlElement(name = "MATNR")
    private String matnr;
}

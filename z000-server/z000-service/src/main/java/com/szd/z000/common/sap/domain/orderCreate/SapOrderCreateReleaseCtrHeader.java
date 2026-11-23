package com.szd.z000.common.sap.domain.orderCreate;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 生产订单下达-请求抬头
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "HEADER")
@Data
public class SapOrderCreateReleaseCtrHeader {
    /** 生成订单 */
    @XmlElement(name = "AUFNR")
    private String aufnr;
}

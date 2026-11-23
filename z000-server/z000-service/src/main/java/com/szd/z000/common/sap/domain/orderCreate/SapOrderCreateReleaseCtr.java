package com.szd.z000.common.sap.domain.orderCreate;

import lombok.Data;

import javax.xml.bind.annotation.*;

/**
 * 生产订单下达-请求
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapOrderCreateReleaseCtr {
    /** 抬头 */
    @XmlElement(name = "HEADER")
    private SapOrderCreateReleaseCtrHeader header;
}

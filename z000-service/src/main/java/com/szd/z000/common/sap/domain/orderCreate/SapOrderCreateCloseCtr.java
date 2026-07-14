package com.szd.z000.common.sap.domain.orderCreate;

import lombok.Data;

import javax.xml.bind.annotation.*;

/**
 * 生产订单关闭-请求
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapOrderCreateCloseCtr {
    /** 抬头 */
    @XmlElement(name = "HEADER")
    private SapOrderCreateCloseCtrHeader header;
}

package com.szd.z000.common.sap.domain.orderCreate;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 生产订单关闭-请求抬头
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "HEADER")
@Data
public class SapOrderCreateCloseCtrHeader {
    /** 订单号 */
    @XmlElement(name = "AUFNR")
    private String aufnr;
    /** 操作类型 C技术关闭/R取消关闭 */
    @XmlElement(name = "ZTYPE")
    private String ztype;
}

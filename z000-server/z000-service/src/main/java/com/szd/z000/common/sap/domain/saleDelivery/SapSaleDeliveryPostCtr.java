package com.szd.z000.common.sap.domain.saleDelivery;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 销售交货单-发货过账请求
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapSaleDeliveryPostCtr {
    /**
     * 抬头
     */
    @XmlElement(name = "HEADER")
    private SapSaleDeliveryPostCtrHeader header;
}

package com.szd.z000.common.sap.domain.saleDelivery;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 销售交货单-抬头
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "HEADER")
@Data
public class SapSaleDeliveryCtrHeader {
    @XmlElement(name = "IF_DOCNO")
    private String ifDocno;
    @XmlElement(name = "ZTYPE")
    private String ztype;
    @XmlElement(name = "VSTEL")
    private String vstel;
    @XmlElement(name = "LEDAT")
    private String ledat;
}

package com.szd.z000.common.sap.domain.saleDelivery;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 销售交货单-发货过账返回
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapSaleDeliveryPostCtrRet {
    @XmlElement(name = "MBLNR")
    private String mblnr;
    @XmlElement(name = "MJAHR")
    private String mjahr;
    @XmlElement(name = "ZSTAU")
    private String zstau;
    @XmlElement(name = "ZMESS")
    private String zmess;
}

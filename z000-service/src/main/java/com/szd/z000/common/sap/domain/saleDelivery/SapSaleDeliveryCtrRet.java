package com.szd.z000.common.sap.domain.saleDelivery;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 销售交货单-返回
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapSaleDeliveryCtrRet {
    @XmlElement(name = "IF_DOCNO")
    private String ifDocno;
    @XmlElement(name = "VBELN")
    private String vbeln;
    @XmlElement(name = "ZSTAU")
    private String zstau;
    @XmlElement(name = "ZMESS")
    private String zmess;
}

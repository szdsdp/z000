package com.szd.z000.common.sap.domain.saleDelivery;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 销售交货单-发货过账抬头
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "HEADER")
@Data
public class SapSaleDeliveryPostCtrHeader {
    /**
     * 业务单号
     */
    @XmlElement(name = "IF_DOCNO")
    private String ifDocno;
    /**
     * 交货单号
     */
    @XmlElement(name = "VBELN_VL")
    private String vbelnVl;
    /**
     * 拣配日期
     */
    @XmlElement(name = "KODAT")
    private String kodat;
    /**
     * 过账日期
     */
    @XmlElement(name = "WADAT_IST")
    private String wadatIst;
    /**
     * 发货日期
     */
    @XmlElement(name = "WADAT")
    private String wadat;
    /**
     * 过账标识(X=发货过账/C=冲销)
     */
    @XmlElement(name = "WABUC")
    private String wabuc;
}

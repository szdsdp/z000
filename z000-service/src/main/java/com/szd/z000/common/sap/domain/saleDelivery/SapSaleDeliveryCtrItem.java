package com.szd.z000.common.sap.domain.saleDelivery;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

/**
 * 销售交货单-行项目
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
@Data
public class SapSaleDeliveryCtrItem {
    @XmlElement(name = "REF_DOC")
    private String refDoc;
    @XmlElement(name = "REF_ITEM")
    private String refItem;
    @XmlElement(name = "DLV_QTY")
    private BigDecimal dlvQty;
    @XmlElement(name = "SALES_UNIT")
    private String salesUnit;
}

package com.szd.z000.common.sap.domain.saleDelivery;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 销售交货单-请求
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapSaleDeliveryCtr {
    @XmlElement(name = "HEADER")
    private SapSaleDeliveryCtrHeader header;
    @XmlElementWrapper(name = "ITEMS")
    @XmlElement(name = "item")
    private List<SapSaleDeliveryCtrItem> itemList;
}

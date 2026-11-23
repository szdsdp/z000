package com.szd.z000.common.sap.domain.saleDeliverySearch;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 交货单抽取-返回
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapSaleDeliverySearchCtrRet {
    /** 返回抬头 */
    @XmlElement(name = "HEADER")
    private SapSaleDeliverySearchCtrRetHeader header;
    /** 交货单清单 */
    @XmlElementWrapper(name = "ITEMS")
    @XmlElement(name = "item")
    private List<SapSaleDeliverySearchCtrRetItem> items;
}

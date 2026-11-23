package com.szd.z000.common.sap.domain.saleOrderSearch;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 销售订单抽取-返回
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapSaleOrderSearchCtrRet {
    /** 返回抬头 */
    @XmlElement(name = "HEADER")
    private SapSaleOrderSearchCtrRetHeader header;
    /** 销售订单清单 */
    @XmlElementWrapper(name = "ITEMS")
    @XmlElement(name = "item")
    private List<SapSaleOrderSearchCtrRetItem> items;
}

package com.szd.z000.common.sap.domain.orderSearch;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 采购订单抽取-返回
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapOrderSearchCtrRet {
    /** 返回抬头 */
    @XmlElement(name = "HEADER")
    private SapOrderSearchCtrRetHeader header;
    /** 物料凭证清单 */
    @XmlElementWrapper(name = "ITEMS")
    @XmlElement(name = "item")
    private List<SapOrderSearchCtrRetItem> items;
}

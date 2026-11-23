package com.szd.z000.common.sap.domain.mtlStock;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 物料库存查询-返回
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapMtlStockCtrRet {
    /** 返回抬头 */
    @XmlElement(name = "HEADER")
    private SapMtlStockCtrRetHeader header;
    /** 库存清单 */
    @XmlElementWrapper(name = "ITEMS")
    @XmlElement(name = "item")
    private List<SapMtlStockCtrRetItem> items;
}

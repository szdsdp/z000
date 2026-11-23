package com.szd.z000.common.sap.domain.saleOrder;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 销售订单创建-请求
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapSaleOrderCtr {
    /**
     * 抬头
     */
    @XmlElement(name = "HEADER")
    private SapSaleOrderCtrHeader header;
    /**
     * 行项目
     */
    @XmlElementWrapper(name = "ITEMS")
    @XmlElement(name = "item")
    private List<SapSaleOrderCtrItem> itemList;
    /**
     * 交货计划
     */
    @XmlElementWrapper(name = "SCHEDS")
    @XmlElement(name = "item")
    private List<SapSaleOrderCtrSched> schedList;
}

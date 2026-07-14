package com.szd.z000.common.sap.domain.purOrder;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;

/**
 * 采购订单创建-交货计划
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "sched")
@Data
public class SapPurOrderCtrSched {
    /** 采购凭证的项目编号 */
    @XmlElement(name = "PO_ITEM")
    private String poItem;
    /** 交货计划行号 */
    @XmlElement(name = "SCHED_LINE")
    private String schedLine;
    /** 交货日期 */
    @XmlElement(name = "DELIVERY_DATE")
    private String deliveryDate;
    /** 已计划数量 */
    @XmlElement(name = "QUANTITY")
    private BigDecimal quantity;
}

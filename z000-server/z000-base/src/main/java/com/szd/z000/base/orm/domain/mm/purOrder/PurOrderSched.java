package com.szd.z000.base.orm.domain.mm.purOrder;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购订单-交货计划
 */
@Data
public class PurOrderSched {
    /**
     * 采购订单ID
     */
    private String orderId;
    /**
     * 项目号(PO_ITEM)
     */
    private int itemNo;
    /**
     * 交货计划行号
     */
    private int schedLine;
    /**
     * 交货日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date deliveryDate;
    /**
     * 已计划数量
     */
    private BigDecimal quantity;
}

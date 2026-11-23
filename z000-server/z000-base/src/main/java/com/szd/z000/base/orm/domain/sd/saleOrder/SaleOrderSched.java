package com.szd.z000.base.orm.domain.sd.saleOrder;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 销售订单创建-交货计划
 */
@Data
public class SaleOrderSched {
    /**
     * 销售订单ID
     */
    private String orderId;
    /**
     * 行项目号
     */
    private int itemNo;
    /**
     * 计划行号
     */
    private int schedNo;
    /**
     * 计划到货日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date reqDate;
    /**
     * 数量
     */
    private BigDecimal reqQty;
}

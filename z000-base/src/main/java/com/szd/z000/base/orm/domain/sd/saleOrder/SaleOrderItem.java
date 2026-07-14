package com.szd.z000.base.orm.domain.sd.saleOrder;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 销售订单创建-行项目
 */
@Data
public class SaleOrderItem {
    /**
     * 销售订单ID
     */
    private String orderId;
    /**
     * 行项目号
     */
    private int itemNo;
    /**
     * 物料编码
     */
    private String mtlCode;
    private String mtlName;
    /**
     * 数量
     */
    private BigDecimal entryQnt;
    /**
     * 单位
     */
    private String unit;
    private String unitName;
    /**
     * 交货日期
     */
    @com.fasterxml.jackson.annotation.JsonFormat(shape = com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private java.util.Date deliveryDate;
    /**
     * 行文本
     */
    private String itemText;
}

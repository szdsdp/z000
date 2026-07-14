package com.szd.z000.base.orm.domain.sd.saleDelivery;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 销售交货单-行项目
 */
@Data
public class SaleDeliveryItem {
    /**
     * 交货单ID
     */
    private String deliveryId;
    /**
     * 行项目号
     */
    private int itemNo;
    /**
     * 销售订单号
     */
    private String orderNo;
    /**
     * 销售订单行号
     */
    private String orderItemNo;
    /**
     * 物料编码
     */
    private String mtlCode;
    /**
     * 物料名称（非持久化）
     */
    private String mtlName;
    /**
     * 销售订单行数量
     */
    private BigDecimal orderQty;
    /**
     * 交货数量
     */
    private BigDecimal dlvQty;
    /**
     * 单位
     */
    private String unit;
    /**
     * 单位名称（非持久化）
     */
    private String unitName;
    /**
     * 未交货数量（非持久化，订单数量-已交货总量）
     */
    private BigDecimal undeliveredQty;
}

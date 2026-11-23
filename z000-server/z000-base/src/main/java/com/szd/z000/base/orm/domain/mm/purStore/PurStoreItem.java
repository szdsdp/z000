package com.szd.z000.base.orm.domain.mm.purStore;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 物料凭证-行项目
 */
@Data
public class PurStoreItem {
    /**
     * 物料凭证ID
     */
    private String storeId;
    /**
     * 项目号
     */
    private int itemNo;
    /**
     * 物料编码
     */
    private String mtlCode;
    private String mtlName;
    /**
     * 工厂
     */
    private String fact;
    private String factName;
    /**
     * 库存地点
     */
    private String store;
    private String storeName;
    /**
     * 数量
     */
    private BigDecimal quantity;
    /**
     * 采购订单行数量
     */
    private BigDecimal orderQty;
    /**
     * 单位
     */
    private String unit;
    private String unitName;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 订单项目编号
     */
    private String orderItemNo;
    /**
     * 前置单据业务单号
     */
    private String refBussDocId;
    /**
     * 前置单据业务类型
     */
    private String refBstp;
    /**
     * 项目文本
     */
    private String itemText;
}

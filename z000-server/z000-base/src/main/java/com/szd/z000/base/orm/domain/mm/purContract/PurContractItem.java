package com.szd.z000.base.orm.domain.mm.purContract;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 采购合同-行项目
 */
@Data
public class PurContractItem {
    /**
     * 采购合同ID
     */
    private String contractId;
    /**
     * 行号
     */
    private String itemNo;
    /**
     * 短文本
     */
    private String itemText;
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
     * 发货存储地点
     */
    private String sendStore;
    /**
     * 需求跟踪号
     */
    private String needNo;
    /**
     * 物料组
     */
    private String mtlGroup;
    private String mtlGroupName;
    /**
     * 目标数量
     */
    private BigDecimal quantity;
    /**
     * 单位
     */
    private String unit;
    /**
     * 净价
     */
    private BigDecimal price;
    /**
     * 价格单位
     */
    private BigDecimal priceUnit;
}

package com.szd.z000.base.orm.domain.pp.orderExtract;

import lombok.Data;

/**
 * 生产订单抽取-组件
 */
@Data
public class OrderExtractModule {
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 行号
     */
    private Integer itemNo;
    /**
     * 预留编号
     */
    private String resNo;
    /**
     * 预留项目
     */
    private String resItem;
    /**
     * 物料编码
     */
    private String mtlCode;
    /**
     * 工厂
     */
    private String fact;
    /**
     * 库存地点
     */
    private String store;
    /**
     * 需求日期
     */
    private String reqDate;
    /**
     * 需求数量
     */
    private String reqQty;
    /**
     * 单位
     */
    private String unit;
}

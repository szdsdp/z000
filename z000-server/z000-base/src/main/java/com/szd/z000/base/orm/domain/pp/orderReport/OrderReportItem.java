package com.szd.z000.base.orm.domain.pp.orderReport;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 生产订单报工-行项目
 */
@Data
public class OrderReportItem {
    /**
     * 生产订单报工ID
     */
    private String reportId;
    /**
     * 项目号
     */
    private int itemNo;
    /**
     * 工序编号
     */
    private String operNo;
    private String operNoName;
    /**
     * 确认文本
     */
    private String confText;
    /**
     * 待确认的产量
     */
    private BigDecimal yield;
    /**
     * 当前要确认废品
     */
    private BigDecimal scrap;
    /**
     * 要确认的当前返工数量
     */
    private BigDecimal rework;
}

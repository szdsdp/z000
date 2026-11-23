package com.szd.z000.base.orm.domain.mm.check.checkPlan;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 库存盘点-行项目
 */
@Data
public class CheckPlanItem {
    /**
     * 盘点单ID
     */
    private String planId;
    /**
     * 行项目号
     */
    private int itemNo;

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
     * 任务状态(非DB字段，由关联任务填充)
     */
    private String taskStatus;
}

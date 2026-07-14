package com.szd.z000.base.orm.domain.mm.check.checkTask;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 盘点任务-行项目
 */
@Data
public class CheckTaskItem {
    /**
     * 任务ID
     */
    private String taskId;
    /**
     * 行号
     */
    private int itemNo;
    /**
     * 物料编码
     */
    private String mtlCode;
    /**
     * 物料名称（非持久化）
     */
    private String mtlName;
    /**
     * 台账数量
     */
    private BigDecimal sysQnt;
    /**
     * 盘点数量
     */
    private BigDecimal countQnt;
    /**
     * 差异数量
     */
    private BigDecimal diffQnt;
    /**
     * 盘点状态(01=正常/02=盘盈/03=盘亏)
     */
    private String countStatus;
    /**
     * 差异说明
     */
    private String diffDesc;
    /**
     * 移动类型(701=盘盈/702=盘亏)
     */
    private String moveType;
    /**
     * 单位
     */
    private String unit;
    /**
     * 盘点人
     */
    private String counter;
    private String counterName;
    /**
     * 行项目文本
     */
    private String itemText;
}

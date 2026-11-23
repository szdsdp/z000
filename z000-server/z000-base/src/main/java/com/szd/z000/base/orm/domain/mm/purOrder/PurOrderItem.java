package com.szd.z000.base.orm.domain.mm.purOrder;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购订单-行项目
 */
@Data
public class PurOrderItem {
    /**
     * 采购订单ID
     */
    private String orderId;
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
     * 采购数量
     */
    private BigDecimal purNum;
    /**
     * 单位
     */
    private String unit;
    /**
     * 采购申请编号
     */
    private String applyNo;
    /**
     * 采购申请的项目编号
     */
    private int applyNoItem;
    /**
     * 交货日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date deliveryDate;
}

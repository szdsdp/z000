package com.szd.z000.base.orm.domain.mm.invCheck;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 发票校验-行项目
 */
@Data
public class InvCheckItem {
    /**
     * 发票校验ID
     */
    private String invId;
    /**
     * 行号
     */
    private int itemNo;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 订单项目编号
     */
    private String orderItemNo;
    /**
     * 物料编码
     */
    private String mtlCode;
    /**
     * 物料名称
     */
    private String mtlName;
    /**
     * 税码
     */
    private String taxCode;
    /**
     * 凭证货币金额
     */
    private BigDecimal amt;
    /**
     * 税额
     */
    private BigDecimal taxAmount;
    /**
     * 数量
     */
    private BigDecimal quantity;
    /**
     * 单位
     */
    private String unit;
    /**
     * 项目文本
     */
    private String itemText;
}

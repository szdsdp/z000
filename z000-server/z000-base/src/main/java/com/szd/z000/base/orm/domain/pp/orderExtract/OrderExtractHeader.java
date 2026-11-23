package com.szd.z000.base.orm.domain.pp.orderExtract;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 生产订单抽取-订单头
 */
@Data
public class OrderExtractHeader {
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 订单类型
     */
    private String orderType;
    /**
     * 订单描述
     */
    private String orderDesc;
    /**
     * 生产版本
     */
    private String prodVer;
    /**
     * 基本完成日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date finishDate;
    /**
     * 订单开始日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startDate;
    /**
     * 预留编号
     */
    private String resNo;
    /**
     * 已发货数量
     */
    private BigDecimal shippedQty;
    /**
     * 订单数量
     */
    private BigDecimal orderQty;
    /**
     * 单位
     */
    private String unit;
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
     * 工艺路线编号
     */
    private String routingNo;
    /**
     * 订单状态
     */
    private String orderStatus;
    /**
     * 已收货数量
     */
    private BigDecimal receivedQty;
}

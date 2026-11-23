package com.szd.z000.base.orm.domain.mm.purOrderSend;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购订单推送接收
 */
@Data
public class PurOrderSend {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 数据类型
     */
    private String dtype;
    /**
     * 采购凭证号
     */
    private String ebeln;
    /**
     * 采购凭证的项目编号
     */
    private String ebelp;
    /**
     * 公司代码
     */
    private String bukrs;
    /**
     * 采购凭证类别
     */
    private String bstyp;
    /**
     * 订单类型（采购）
     */
    private String bsart;
    /**
     * 资产类别作删除标记
     */
    private String loekz;
    /**
     * 采购凭证的状态
     */
    private String statu;
    /**
     * 记录的创建日期
     */
    private String aedat;
    /**
     * 创建对象的人员名称
     */
    private String ernam;
    /**
     * 供应商或债权人的帐号
     */
    private String lifnr;
    /**
     * 名称
     */
    private String name1;
    /**
     * 语言代码
     */
    private String spras;
    /**
     * 付款条件代码
     */
    private String zterm;
    /**
     * 采购组织
     */
    private String ekorg;
    /**
     * 采购组
     */
    private String ekgrp;
    /**
     * 货币码
     */
    private String waers;
    /**
     * 汇率
     */
    private BigDecimal wkurs;
    /**
     * 采购订单日期
     */
    private String bedat;
    /**
     * 有效起始日期
     */
    private String kdatb;
    /**
     * 短文本
     */
    private String txz01;
    /**
     * 物料编码
     */
    private String mtlCode;
    /**
     * 物料描述（短文本）
     */
    private String maktx;
    /**
     * 工厂
     */
    private String fact;
    /**
     * 物料组
     */
    private String matkl;
    /**
     * 采购信息记录的编号
     */
    private String infnr;
    /**
     * 数量
     */
    private BigDecimal menge;
    /**
     * 单位
     */
    private String unit;
    /**
     * 创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}

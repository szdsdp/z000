package com.szd.z000.web.sap.mm.orderSearch.domain;

import lombok.Data;

/**
 * 采购订单抽取-结果VO
 */
@Data
public class OrderSearchResultVO {
    /** 采购凭证号 */
    private String ebeln;
    /** 公司代码 */
    private String bukrs;
    /** 订单类型（采购） */
    private String bsart;
    /** 更改日期 */
    private String aedat;
    /** 创建对象的人员名称 */
    private String ernam;
    /** 供应商或债权人的帐号 */
    private String lifnr;
    /** 付款条件代码 */
    private String zterm;
    /** 采购组织 */
    private String ekorg;
    /** 采购组 */
    private String ekgrp;
    /** 采购凭证的项目编号 */
    private String ebelp;
    /** 短文本 */
    private String txz01;
    /** 物料编号 */
    private String matnr;
    /** 工厂 */
    private String werks;
    /** 物料组 */
    private String matkl;
    /** 采购订单数量 */
    private String menge;
    /** 基本计量单位 */
    private String meins;
    /** 已收货数量 */
    private String zshsl;
}

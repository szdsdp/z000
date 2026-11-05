package com.szd.z000.common.sap.domain.orderSearch;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 采购订单抽取-返回行项目
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapOrderSearchCtrRetItem {
    /**
     * 采购凭证号
     */
    @XmlElement(name = "EBELN")
    private String ebeln;
    /**
     * 公司代码
     */
    @XmlElement(name = "BUKRS")
    private String bukrs;
    /**
     * 订单类型（采购）
     */
    @XmlElement(name = "BSART")
    private String bsart;
    /**
     * 更改日期
     */
    @XmlElement(name = "AEDAT")
    private String aedat;
    /**
     * 创建对象的人员名称
     */
    @XmlElement(name = "ERNAM")
    private String ernam;
    /**
     * 供应商或债权人的帐号
     */
    @XmlElement(name = "LIFNR")
    private String lifnr;
    /**
     * 付款条件代码
     */
    @XmlElement(name = "ZTERM")
    private String zterm;
    /**
     * 采购组织
     */
    @XmlElement(name = "EKORG")
    private String ekorg;
    /**
     * 采购组
     */
    @XmlElement(name = "EKGRP")
    private String ekgrp;
    /**
     * 采购凭证的项目编号
     */
    @XmlElement(name = "EBELP")
    private String ebelp;
    /**
     * 短文本
     */
    @XmlElement(name = "TXZ01")
    private String txz01;
    /**
     * 物料编号
     */
    @XmlElement(name = "MATNR")
    private String matnr;
    /**
     * 工厂
     */
    @XmlElement(name = "WERKS")
    private String werks;
    /**
     * 工厂名称
     */
    @XmlElement(name = "WERKS_T")
    private String werksT;
    /**
     * 物料组
     */
    @XmlElement(name = "MATKL")
    private String matkl;
    /**
     * 采购订单数量
     */
    @XmlElement(name = "MENGE")
    private String menge;
    /**
     * 基本计量单位
     */
    @XmlElement(name = "MEINS")
    private String meins;
    /**
     * 已收货数量
     */
    @XmlElement(name = "ZSHSL")
    private String zshsl;
}

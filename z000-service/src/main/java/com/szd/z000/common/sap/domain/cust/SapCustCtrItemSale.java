package com.szd.z000.common.sap.domain.cust;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 客商维护-销售组织信息
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
@Data
public class SapCustCtrItemSale {

    /**
     * 销售组织
     */
    @XmlElement(name = "VKORG")
    private String saleOrg;

    /**
     * 分销渠道
     */
    @XmlElement(name = "VTWEG")
    private String vtweg;

    /**
     * 产品组
     */
    @XmlElement(name = "SPART")
    private String spart;
    /**
     * 货币
     */
    @XmlElement(name = "WAERS")
    private String curr;

    /**
     * 客户定价过程/定价是否含税
     */
    @XmlElement(name = "KALKS")
    private String priceTax;

    /**
     * 客户分类
     */
    @XmlElement(name = "PLTYP")
    private String custClass;

    /**
     * 客户二级分类
     */
    @XmlElement(name = "KDGRP")
    private String custClassSub;

    /**
     * 付款条件代码
     */
    @XmlElement(name = "ZTERM")
    private String payTerms;

    /**
     * 客户的科目分配组
     */
    @XmlElement(name = "KTGRD")
    private String accsGrp;

    /**
     * 国际贸易条款
     */
    @XmlElement(name = "INCO1")
    private String incoterms;

    /**
     * 国际贸易条款位置 1
     */
    @XmlElement(name = "INCO2_L")
    private String incoterms1;

    /**
     * 销售部门
     */
    @XmlElement(name = "BZIRK")
    private String saleDept;

    /**
     * 销售大区
     */
    @XmlElement(name = "KVGR1")
    private String saleRegion;
    /**
     * 装运条件
     */
    @XmlElement(name = "VSBED")
    private String shippingTerms;

    /**
     * 信用控制范围
     */
    @XmlElement(name = "KKBER")
    private String creditCtrlArea;

    /**
     * 客户-销售组织-业务伙伴
     */
    @XmlElementWrapper(name = "ITEM_KNVP")
    @XmlElement(name = "item")
    private List<SapCustCtrItemSalePartner> partnerList;
}

package com.szd.z000.common.sap.domain.storeSearch;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 物料凭证抽取-返回行项目
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapStoreSearchCtrRetItem {
    /** 采购凭证号 */
    @XmlElement(name = "EBELN")
    private String ebeln;
    /** 公司代码 */
    @XmlElement(name = "BUKRS")
    private String bukrs;
    /** 供应商或债权人的帐号 */
    @XmlElement(name = "LIFNR")
    private String lifnr;
    /** 采购凭证类型 */
    @XmlElement(name = "BSART")
    private String bsart;
    /** 付款条件代码 */
    @XmlElement(name = "ZTERM")
    private String zterm;
    /** 采购组织 */
    @XmlElement(name = "EKORG")
    private String ekorg;
    /** 采购组 */
    @XmlElement(name = "EKGRP")
    private String ekgrp;
    /** 货币码 */
    @XmlElement(name = "WAERS")
    private String waers;
    /** 采购凭证日期 */
    @XmlElement(name = "BEDAT")
    private String bedat;
    /** 单据条件数 */
    @XmlElement(name = "KNUMV")
    private String knumv;
    /** 过程（定价、输出控制、科目、细节、成本...） */
    @XmlElement(name = "KALSM")
    private String kalsm;
    /** 采购凭证的项目编号 */
    @XmlElement(name = "EBELP")
    private String ebelp;
    /** 物料编号 */
    @XmlElement(name = "MATNR")
    private String matnr;
    /** 短文本 */
    @XmlElement(name = "MATNR_T")
    private String matnrT;
    /** 工厂 */
    @XmlElement(name = "WERKS")
    private String werks;
    /** 名称 */
    @XmlElement(name = "WERKS_T")
    private String werksT;
    /** 库存地点 */
    @XmlElement(name = "LGORT")
    private String lgort;
    /** 物料组 */
    @XmlElement(name = "MATKL")
    private String matkl;
    /** 采购订单数量 */
    @XmlElement(name = "MENGE")
    private String menge;
    /** 订单单位 */
    @XmlElement(name = "MEINS")
    private String meins;
    /** 销售/购买税代码 */
    @XmlElement(name = "MWSKZ")
    private String mwskz;
    /** 税率 */
    @XmlElement(name = "MWSKZ_V")
    private String mwskzV;
    /** 凭证货币金额 */
    @XmlElement(name = "WRBTR_JE")
    private String wrbtrJe;
    /** 凭证货币金额 */
    @XmlElement(name = "WRBTR_HS")
    private String wrbtrHs;
    /** 凭证货币金额 */
    @XmlElement(name = "WRBTR_SE")
    private String wrbtrSe;
    /** 凭证货币金额 */
    @XmlElement(name = "WRBTR_DJ")
    private String wrbtrDj;
    /** 数量 */
    @XmlElement(name = "M_MENGE")
    private String mMenge;
    /** 凭证货币金额 */
    @XmlElement(name = "M_WRBTR_JE")
    private String mWrbtrJe;
    /** 凭证货币金额 */
    @XmlElement(name = "M_WRBTR_HS")
    private String mWrbtrHs;
    /** 凭证货币金额 */
    @XmlElement(name = "M_WRBTR_SE")
    private String mWrbtrSe;
    /** 数量 */
    @XmlElement(name = "F_MENGE")
    private String fMenge;
    /** 凭证货币金额 */
    @XmlElement(name = "F_WRBTR_JE")
    private String fWrbtrJe;
    /** 凭证货币金额 */
    @XmlElement(name = "F_WRBTR_HS")
    private String fWrbtrHs;
    /** 凭证货币金额 */
    @XmlElement(name = "F_WRBTR_SE")
    private String fWrbtrSe;
}

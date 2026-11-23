package com.szd.z000.common.sap.domain.saleOrderSearch;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 销售订单抽取-返回行项目
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapSaleOrderSearchCtrRetItem {
    /** 销售凭证 */
    @XmlElement(name = "VBELN")
    private String vbeln;
    /** 记录的创建日期 */
    @XmlElement(name = "ERDAT")
    private String erdat;
    /** 销售凭证类型 */
    @XmlElement(name = "AUART")
    private String auart;
    /** 销售机构 */
    @XmlElement(name = "VKORG")
    private String vkorg;
    /** 分销渠道 */
    @XmlElement(name = "VTWEG")
    private String vtweg;
    /** 客户编号 */
    @XmlElement(name = "KUNNR")
    private String kunnr;
    /** 销售凭证项目 */
    @XmlElement(name = "POSNR")
    private String posnr;
    /** 物料编号 */
    @XmlElement(name = "MATNR")
    private String matnr;
    /** 物料组 */
    @XmlElement(name = "MATKL")
    private String matkl;
    /** 销售订单项目短文本 */
    @XmlElement(name = "ARKTX")
    private String arktx;
    /** 销售单位目标数量 */
    @XmlElement(name = "KWMENG")
    private String kwmeng;
    /** 目标数量计量单位 */
    @XmlElement(name = "ZIEME")
    private String zieme;
    /** 已创建交货单数量 */
    @XmlElement(name = "ZJHDS")
    private String zjhds;
    /** 已交货数量 */
    @XmlElement(name = "ZFHDS")
    private String zfhds;
}

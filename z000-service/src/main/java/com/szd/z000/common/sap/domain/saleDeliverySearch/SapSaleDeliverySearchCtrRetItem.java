package com.szd.z000.common.sap.domain.saleDeliverySearch;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 交货单抽取-返回行项目
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapSaleDeliverySearchCtrRetItem {
    /** 交货 */
    @XmlElement(name = "VBELN_VL")
    private String vbelnVl;
    /** 记录的创建日期 */
    @XmlElement(name = "ERDAT")
    private String erdat;
    /** 销售地区 */
    @XmlElement(name = "BZIRK")
    private String bzirk;
    /** 装运点/接收点 */
    @XmlElement(name = "VSTEL")
    private String vstel;
    /** 销售机构 */
    @XmlElement(name = "VKORG")
    private String vkorg;
    /** 交货类型 */
    @XmlElement(name = "LFART")
    private String lfart;
    /** 装载日期 */
    @XmlElement(name = "LDDAT")
    private String lddat;
    /** 交货日期 */
    @XmlElement(name = "LFDAT")
    private String lfdat;
    /** 拣配日期 */
    @XmlElement(name = "KODAT")
    private String kodat;
    /** 送达方 */
    @XmlElement(name = "KUNNR")
    private String kunnr;
    /** 售达方 */
    @XmlElement(name = "KUNAG")
    private String kunag;
    /** 交货项目 */
    @XmlElement(name = "POSNR")
    private String posnr;
    /** 物料编号 */
    @XmlElement(name = "MATNR")
    private String matnr;
    /** 物料组 */
    @XmlElement(name = "MATKL")
    private String matkl;
    /** 工厂 */
    @XmlElement(name = "WERKS")
    private String werks;
    /** 库存地点 */
    @XmlElement(name = "LGORT")
    private String lgort;
    /** 实际已交货量（按销售单位） */
    @XmlElement(name = "LFIMG")
    private String lfimg;
    /** 销售单位 */
    @XmlElement(name = "VRKME")
    private String vrkme;
    /** 销售凭证 */
    @XmlElement(name = "VBELN_VA")
    private String vbelnVa;
    /** 开票凭证 */
    @XmlElement(name = "VBELN_VF")
    private String vbelnVf;
    /** 开票类型 */
    @XmlElement(name = "FKART")
    private String fkart;
    /** 出具发票项目 */
    @XmlElement(name = "POSNR_VF")
    private String posnrVf;
    /** 实际已开票数量 */
    @XmlElement(name = "FKIMG")
    private String fkimg;
}

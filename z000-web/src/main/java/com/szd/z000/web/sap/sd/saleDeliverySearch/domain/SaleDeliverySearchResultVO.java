package com.szd.z000.web.sap.sd.saleDeliverySearch.domain;

import lombok.Data;

/**
 * 交货单抽取-结果VO
 */
@Data
public class SaleDeliverySearchResultVO {
    /** 交货 */
    private String vbelnVl;
    /** 记录的创建日期 */
    private String erdat;
    /** 销售地区 */
    private String bzirk;
    /** 装运点/接收点 */
    private String vstel;
    /** 销售机构 */
    private String vkorg;
    /** 交货类型 */
    private String lfart;
    /** 装载日期 */
    private String lddat;
    /** 交货日期 */
    private String lfdat;
    /** 拣配日期 */
    private String kodat;
    /** 送达方 */
    private String kunnr;
    /** 售达方 */
    private String kunag;
    /** 交货项目 */
    private String posnr;
    /** 物料编号 */
    private String matnr;
    /** 物料组 */
    private String matkl;
    /** 工厂 */
    private String werks;
    /** 库存地点 */
    private String lgort;
    /** 实际已交货量（按销售单位） */
    private String lfimg;
    /** 销售单位 */
    private String vrkme;
    /** 销售凭证 */
    private String vbelnVa;
    /** 开票凭证 */
    private String vbelnVf;
    /** 开票类型 */
    private String fkart;
    /** 出具发票项目 */
    private String posnrVf;
    /** 实际已开票数量 */
    private String fkimg;
}

package com.szd.z000.web.sap.sd.saleOrderSearch.domain;

import lombok.Data;

/**
 * 销售订单抽取-结果VO
 */
@Data
public class SaleOrderSearchResultVO {
    /** 销售凭证 */
    private String vbeln;
    /** 记录的创建日期 */
    private String erdat;
    /** 销售凭证类型 */
    private String auart;
    /** 销售机构 */
    private String vkorg;
    /** 分销渠道 */
    private String vtweg;
    /** 客户编号 */
    private String kunnr;
    /** 销售凭证项目 */
    private String posnr;
    /** 物料编号 */
    private String matnr;
    /** 物料组 */
    private String matkl;
    /** 销售订单项目短文本 */
    private String arktx;
    /** 销售单位目标数量 */
    private String kwmeng;
    /** 目标数量计量单位 */
    private String zieme;
    /** 已创建交货单数量 */
    private String zjhds;
    /** 已交货数量 */
    private String zfhds;
}

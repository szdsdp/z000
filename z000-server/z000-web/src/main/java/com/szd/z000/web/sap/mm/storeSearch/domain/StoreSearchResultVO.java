package com.szd.z000.web.sap.mm.storeSearch.domain;

import lombok.Data;

/**
 * 物料凭证抽取-结果VO
 */
@Data
public class StoreSearchResultVO {
    /** 采购凭证号 */
    private String ebeln;
    /** 公司代码 */
    private String bukrs;
    /** 供应商或债权人的帐号 */
    private String lifnr;
    /** 采购凭证类型 */
    private String bsart;
    /** 付款条件代码 */
    private String zterm;
    /** 采购组织 */
    private String ekorg;
    /** 采购组 */
    private String ekgrp;
    /** 货币码 */
    private String waers;
    /** 采购凭证日期 */
    private String bedat;
    /** 单据条件数 */
    private String knumv;
    /** 过程（定价、输出控制、科目、细节、成本...） */
    private String kalsm;
    /** 采购凭证的项目编号 */
    private String ebelp;
    /** 物料编号 */
    private String matnr;
    /** 短文本 */
    private String matnrT;
    /** 工厂 */
    private String werks;
    /** 名称 */
    private String werksT;
    /** 库存地点 */
    private String lgort;
    /** 物料组 */
    private String matkl;
    /** 采购订单数量 */
    private String menge;
    /** 订单单位 */
    private String meins;
    /** 销售/购买税代码 */
    private String mwskz;
    /** 税率 */
    private String mwskzV;
    /** 凭证货币金额(JE) */
    private String wrbtrJe;
    /** 凭证货币金额(HS) */
    private String wrbtrHs;
    /** 凭证货币金额(SE) */
    private String wrbtrSe;
    /** 凭证货币金额(DJ) */
    private String wrbtrDj;
    /** 入库数量 */
    private String mMenge;
    /** 入库金额(JE) */
    private String mWrbtrJe;
    /** 入库金额(HS) */
    private String mWrbtrHs;
    /** 入库金额(SE) */
    private String mWrbtrSe;
    /** 发票数量 */
    private String fMenge;
    /** 发票金额(JE) */
    private String fWrbtrJe;
    /** 发票金额(HS) */
    private String fWrbtrHs;
    /** 发票金额(SE) */
    private String fWrbtrSe;
}

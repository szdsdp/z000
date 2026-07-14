package com.szd.z000.web.sap.mm.suppRecon.domain;

import lombok.Data;

/**
 * 供应商对账查询-结果VO
 */
@Data
public class SuppReconResultVO {
    /** 公司代码 */
    private String bukrs;
    /** 供应商 */
    private String lifnr;
    /** 供应商描述 */
    private String lifnrTxt;
    /** 会计凭证 */
    private String belnrFi;
    /** 发票凭证 */
    private String belnrR;
    /** 发票年度 */
    private String gjahrR;
    /** 凭证类型 */
    private String blartFi;
    /** 过账日期 */
    private String budatFi;
    /** 发票日期 */
    private String bldatR;
    /** 货币 */
    private String waers;
    /** 原币金额(含税) */
    private String dmbtrY;
    /** 本币金额(含税) */
    private String dmbtrL;
    /** 发票金额(含税) */
    private String dmbtrMm;
    /** 财务金额(含税) */
    private String dmbtrFi;
    /** 应付 */
    private String dmbtr;
    /** 余额 */
    private String dmbtrYe;
    /** 抬头文本 */
    private String bktxt;
    /** 年度 */
    private String gjahrFi;
    /** 参照 */
    private String xblnrFi;
    /** 参考码 */
    private String awkeyFi;
    /** 冲销凭证 */
    private String stblgFi;
    /** 冲销年度 */
    private String stjahFi;
    /** 汇率 */
    private String kursf;
    /** 行项目 */
    private String buzei;
    /** 发票凭证状态 */
    private String rbstatR;
    /** 被冲销发票凭证 */
    private String stblgR;
    /** 冲销年度 */
    private String stjahR;
}

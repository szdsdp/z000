package com.szd.z000.web.sap.sd.custRecon.domain;

import lombok.Data;

/**
 * 客商对账查询-结果VO
 */
@Data
public class CustReconResultVO {
    /** 公司代码 */
    private String bukrs;
    /** 客户 */
    private String kunnr;
    /** 客户描述 */
    private String kunnrTxt;
    /** 会计凭证 */
    private String belnrFi;
    /** 发票凭证 */
    private String vbeln;
    /** 凭证类型 */
    private String blartFi;
    /** 开票类型 */
    private String fkart;
    /** 过账日期 */
    private String budatFi;
    /** 发票日期 */
    private String fkdat;
    /** 货币 */
    private String waers;
    /** 原币金额(含税) */
    private String dmbtrY;
    /** 本币金额(含税) */
    private String dmbtrL;
    /** 发票金额(含税) */
    private String dmbtrSd;
    /** 财务金额(含税) */
    private String dmbtrFi;
    /** 收入 */
    private String dmbtr;
    /** 余额 */
    private String dmbtrYe;
    /** 抬头文本 */
    private String bktxt;
    /** 销售部 */
    private String bzirk;
    /** 产品组 */
    private String spart;
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
    /** 发票冲销标识 */
    private String fksto;
    /** 被冲销发票凭证 */
    private String sfakn;
}

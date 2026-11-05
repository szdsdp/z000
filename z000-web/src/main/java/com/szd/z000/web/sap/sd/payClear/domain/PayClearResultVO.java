package com.szd.z000.web.sap.sd.payClear.domain;

import lombok.Data;

/**
 * 客商未清项抽取-结果VO
 */
@Data
public class PayClearResultVO {
    /** 公司代码 */
    private String bukrs;
    /** 会计凭证编号 */
    private String belnr;
    /** 财年 */
    private String gjahr;
    /** 凭证类型 */
    private String blart;
    /** 凭证抬头文本 */
    private String bktxt;
    /** 货币码 */
    private String waers;
    /** 会计凭证中的行项目数 */
    private String buzei;
    /** 记帐代码 */
    private String bschl;
    /** 借方/贷方标识 */
    private String shkzg;
    /** 凭证货币金额 */
    private String wrbtr;
    /** 供应商或债权人的帐号 */
    private String lifnr;
    /** 名称（供应商） */
    private String lifnrT;
    /** 客户编号 */
    private String kunnr;
    /** 名称（客户） */
    private String kunnrT;
    /** 总账科目 */
    private String hkont;
    /** 总帐科目成文本 */
    private String hkontT;
    /** 分配编号 */
    private String zuonr;
    /** 项目文本 */
    private String sgtxt;
}

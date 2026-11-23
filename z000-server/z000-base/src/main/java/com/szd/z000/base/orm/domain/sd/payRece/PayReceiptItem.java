package com.szd.z000.base.orm.domain.sd.payRece;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 回款认领-行项目（未清项）
 */
@Data
public class PayReceiptItem {
    /**
     * 主键id
     */
    private String receiptId;
    /**
     * 行号
     */
    private int itemNo;
    /**
     * 清账金额
     */
    private BigDecimal clearAmt;
    /**
     * 公司代码
     */
    private String cmpy;
    /**
     * 凭证号
     */
    private String vchNum;
    /**
     * 财年
     */
    private String vchYear;
    /**
     * 凭证行号
     */
    private String vchNumItem;
    /**
     * 凭证类型
     */
    private String blart;
    /**
     * 凭证抬头文本
     */
    private String bkTxt;
    /**
     * 货币码
     */
    private String curr;
    /**
     * 记帐代码
     */
    private String bschl;
    /**
     * 借方/贷方标识
     */
    private String drCr;
    /**
     * 凭证货币金额
     */
    private String amt;
    /**
     * 供应商或债权人的帐号
     */
    private String lifnr;
    /**
     * 名称（供应商）
     */
    private String lifnrT;
    /**
     * 客户编号
     */
    private String cust;
    /**
     * 名称（客户）
     */
    private String custName;
    /**
     * 总账科目
     */
    private String accs;
    /**
     * 科目描述
     */
    private String accsName;
    /**
     * 分配编号
     */
    private String zuonr;
    /**
     * 项目文本
     */
    private String sgtxt;
}

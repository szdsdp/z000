package com.szd.z000.base.orm.domain.sd.payRece;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 回款认领-票据展示
 */
@Data
public class PayReceiptBill {
    /**
     * 系统票号
     */
    private String bankDoc;
    /**
     * 持票账户
     */
    private String holderAcc;
    /**
     * 票包号
     */
    private String billNo;
    /**
     * 子票区间从
     */
    private String subBillFrom;
    /**
     * 子票区间至
     */
    private String subBillTo;
    /**
     * 出票日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date issueDate;
    /**
     * 到票日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dueDate;
    /**
     * 票据类型
     */
    private String billType;
    private String billTypeName;
    /**
     * 票据范围
     */
    private String billScope;
    private String billScopeName;
    /**
     * 金额
     */
    private BigDecimal amt;
    /**
     * 承兑人
     */
    private String acceptor;
    /**
     * 出票人
     */
    private String drawer;
    /**
     * 收票人
     */
    private String payee;
    /**
     * 签收日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date signDate;
    /**
     * 前手账号
     */
    private String endorserAcc;
    /**
     * 前手账号名称
     */
    private String endorser;
    /**
     * 摘要
     */
    private String summary;
}

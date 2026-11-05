package com.szd.z000.base.orm.domain.sd.payFlowBill;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.szd.core.client.domain.common.ClientPageEntity;
import com.szd.z000.base.common.excel.ExcelExport;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 票据承兑
 */
@Data
public class PayFlowBill extends ClientPageEntity {
    /**
     * 系统票号
     */
    @ExcelExport(name = "系统票号")
    private String sysBillNo;

    /**
     * 票包号
     */
    @ExcelExport(name = "票包号")
    private String billNo;

    /**
     * 子票区间从
     */
    @ExcelExport(name = "子票区间从")
    private String subBillFrom;

    /**
     * 子票区间至
     */
    @ExcelExport(name = "子票区间至")
    private String subBillTo;

    /**
     * 公司代码
     */
    @ExcelExport(name = "公司代码")
    private String cmpy;
    private String cmpyName;

    /**
     * 持票账户
     */
    @ExcelExport(name = "持票账户")
    private String holderAcc;

    /**
     * 前手账号
     */
    @ExcelExport(name = "前手账号")
    private String endorserAcc;

    /**
     * 前手账号名称
     */
    @ExcelExport(name = "前手账号名称")
    private String endorser;

    /**
     * 出票人
     */
    @ExcelExport(name = "出票人")
    private String drawer;

    /**
     * 收票人
     */
    @ExcelExport(name = "收票人")
    private String payee;

    /**
     * 金额
     */
    @ExcelExport(name = "金额")
    private BigDecimal amt;

    /**
     * 签收日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date signDate;

    /**
     * 签收日期字符串（导入用）
     */
    @ExcelExport(name = "签收日期(YYYYMMDD)")
    private String signDateStr;

    /**
     * 票据类型
     */
    @ExcelExport(name = "票据类型")
    private String billType;
    private String billTypeName;

    /**
     * 票据范围
     */
    @ExcelExport(name = "票据范围")
    private String billScope;
    private String billScopeName;

    /**
     * 承兑人
     */
    @ExcelExport(name = "承兑人")
    private String acceptor;

    /**
     * 出票日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date issueDate;

    /**
     * 出票日期字符串（导入用）
     */
    @ExcelExport(name = "出票日期(YYYYMMDD)")
    private String issueDateStr;

    /**
     * 到期日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dueDate;

    /**
     * 到期日期字符串（导入用）
     */
    @ExcelExport(name = "到票日期(YYYYMMDD)")
    private String dueDateStr;

    /**
     * 银行摘要
     */
    @ExcelExport(name = "摘要")
    private String summary;

    /**
     * 认领中金额
     */
    private BigDecimal amtProcess;

    /**
     * 已认领金额
     */
    private BigDecimal amtReceipt;

    /**
     * 剩余未认领金额
     */
    private BigDecimal amtRemain;

    /**
     * 客户编码
     */
    private String cust;
    private String custName;

    /**
     * 大区
     */
    private String region;
    private String regionName;

    /**
     * 销售经理
     */
    private String salesManager;
    private String salesManagerName;
}

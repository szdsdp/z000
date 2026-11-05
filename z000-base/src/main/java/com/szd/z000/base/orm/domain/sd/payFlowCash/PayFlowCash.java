package com.szd.z000.base.orm.domain.sd.payFlowCash;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.szd.core.client.domain.common.ClientPageEntity;
import com.szd.z000.base.common.excel.ExcelExport;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 电汇回款
 */
@Data
public class PayFlowCash extends ClientPageEntity {
    /**
     * 银行回单
     */
    @ExcelExport(name = "银行回单")
    private String bankDoc;

    /**
     * 公司代码
     */
    @ExcelExport(name = "公司代码")
    private String cmpy;
    private String cmpyName;

    /**
     * 银行流水号
     */
    @ExcelExport(name = "银行流水号")
    private String bankFlowNo;

    /**
     * 本方账户
     */
    @ExcelExport(name = "本方账户")
    private String myAccNo;

    /**
     * 对方账户
     */
    @ExcelExport(name = "对方账户")
    private String payAccNo;

    /**
     * 对方户名
     */
    @ExcelExport(name = "对方户名")
    private String payAccName;

    /**
     * 币种
     */
    @ExcelExport(name = "币种")
    private String curr;
    private String currName;

    /**
     * 回款金额
     */
    @ExcelExport(name = "金额")
    private BigDecimal amt;

    /**
     * 交易日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date tranDate;

    /**
     * 交易日期字符串（导入用）
     */
    @ExcelExport(name = "交易日期")
    private String tranDateStr;

    /**
     * 交易时间
     */
    @ExcelExport(name = "交易时间")
    private String tranTime;

    /**
     * 摘要
     */
    @ExcelExport(name = "摘要")
    private String summary;

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

    /**
     * 认领状态
     */
    private String claimStatus;

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
}

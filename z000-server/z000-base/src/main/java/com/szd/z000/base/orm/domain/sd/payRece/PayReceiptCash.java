package com.szd.z000.base.orm.domain.sd.payRece;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 回款认领-电汇展示
 */
@Data
public class PayReceiptCash {
    /**
     * 银行回单
     */
    private String bankDoc;
    /**
     * 银行流水号
     */
    private String bankFlowNo;
    /**
     * 本方账户
     */
    private String myAccNo;
    /**
     * 对方账户
     */
    private String payAccNo;
    /**
     * 对方户名
     */
    private String payAccName;
    /**
     * 金额
     */
    private BigDecimal amt;
    /**
     * 币种
     */
    private String curr;
    /**
     * 交易日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date tranDate;
    /**
     * 交易时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private Date tranTime;
    /**
     * 摘要
     */
    private String summary;
}

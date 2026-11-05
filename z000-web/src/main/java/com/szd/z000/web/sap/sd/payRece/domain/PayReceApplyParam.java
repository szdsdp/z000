package com.szd.z000.web.sap.sd.payRece.domain;

import lombok.Data;

/**
 * 回款认领申请参数
 */
@Data
public class PayReceApplyParam {
    /**
     * 银行回单号（电汇用）
     */
    private String bankDoc;
    /**
     * 系统票号（票据用）
     */
    private String sysBillNo;
    /**
     * 业务类型
     */
    private String bstp;
    /**
     * 支付方式（B=电汇，P=票据）
     */
    private String payWay;
    /**
     * 币种
     */
    private String curr;
}

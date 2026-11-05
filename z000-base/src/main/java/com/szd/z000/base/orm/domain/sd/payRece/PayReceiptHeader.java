package com.szd.z000.base.orm.domain.sd.payRece;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 回款认领-头
 */
@Data
public class PayReceiptHeader extends ClientWfBussBase {
    /**
     * 主键id
     */
    private String receiptId;
    /**
     * 单据状态
     */
    private String bussStatus;
    private String bussStatusName;

    /**
     * 申请人
     */
    private String userId;
    private String userName;

    /**
     * 业务日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date bussDate;
    /**
     * 币种
     */
    private String curr;
    /**
     * 回款性质
     */
    private String payNature;
    private String payNatureName;
    /**
     * 支付方式
     */
    private String payWay;
    private String payWayName;
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
     * 文本
     */
    private String text;
    /**
     * 利润中心
     */
    private String prfc;
    private String prfcName;
    /**
     * 银行回单/系统票号
     */
    private String bankDoc;
    /**
     * 本次认领金额
     */
    private BigDecimal amtReceipt;
    /**
     * 凭证号
     */
    private String vchNum;
    /**
     * 凭证财年
     */
    private String vchYear;
    /**
     * 冲销凭证号
     */
    private String vchNumReverse;
    /**
     * 冲销凭证财年
     */
    private String vchYearReverse;
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
     * 摘要
     */
    private String summary;
    /**
     * 回款金额
     */
    private BigDecimal amt;
    /**
     * 剩余认领金额
     */
    private BigDecimal amtRemain;
    /**
     * SAP消息id
     */
    private String sapMessId;
    /**
     * SAP消息描述
     */
    private String sapMsg;
}

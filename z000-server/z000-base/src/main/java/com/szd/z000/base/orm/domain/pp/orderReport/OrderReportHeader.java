package com.szd.z000.base.orm.domain.pp.orderReport;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import lombok.Data;

import java.util.Date;

/**
 * 生产订单报工-抬头
 */
@Data
public class OrderReportHeader extends ClientWfBussBase {
    /**
     * 生产订单报工ID
     */
    private String reportId;
    /**
     * 订单号
     */
    private String aufnr;
    /**
     * 操作完成的确认编号
     */
    private String confno;
    /**
     * 确认计数器
     */
    private String confcnt;
    /**
     * 凭证中的过帐日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date budat;
    /**
     * 冲销确认计数器
     */
    private String revConfcnt;
    /**
     * 冲销SAP消息
     */
    private String revSapMsg;
    /**
     * 冲销SAP消息ID
     */
    private String revSapMessId;
    /**
     * SAP消息
     */
    private String sapMsg;
    /**
     * SAP消息ID
     */
    private String sapMessId;

    /**
     * 业务日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date bussDate;
    /**
     * 业务状态
     */
    private String bussStatus;
    private String bussStatusName;
    /**
     * 报工数量（行项目产量汇总，展示用，不存库）
     */
    private java.math.BigDecimal reportQty;
    /**
     * 废品数量（行项目废品汇总，展示用，不存库）
     */
    private java.math.BigDecimal scrapQty;
}

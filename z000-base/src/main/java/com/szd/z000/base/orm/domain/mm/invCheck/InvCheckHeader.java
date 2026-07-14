package com.szd.z000.base.orm.domain.mm.invCheck;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 发票校验-抬头
 */
@Data
public class InvCheckHeader extends ClientWfBussBase {
    /**
     * 发票校验ID
     */
    private String invId;
    /**
     * 凭证日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date vchDate;
    /**
     * 过帐日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date postDate;
    /**
     * 货币码
     */
    private String curr;
    private String currName;
    /**
     * 总发票金额
     */
    private BigDecimal invAmt;
    /**
     * 付款条件
     */
    private String terms;
    /**
     * 基准日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date blineDate;
    /**
     * 付款方式
     */
    private String payWay;
    /**
     * 物料凭证业务单号
     */
    private String storeBussDocId;
    /**
     * SAP消息
     */
    private String sapMsg;
    /**
     * SAP消息ID
     */
    private String sapMessId;
    /**
     * 发票凭证编号
     */
    private String reBelnr;
    /**
     * 财年
     */
    private String reGjahr;
    /**
     * 公司代码(返回)
     */
    private String bukrs;
    /**
     * 会计凭证编号
     */
    private String belnr;
    /**
     * 财年(会计)
     */
    private String gjahr;
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
     * 冲销日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date budat;
    /**
     * 冲销-发票凭证编号
     */
    private String revReBelnr;
    /**
     * 冲销-财年
     */
    private String revReGjahr;
    /**
     * 冲销-会计凭证编号
     */
    private String revBelnr;
    /**
     * 冲销-财年(会计)
     */
    private String revGjahr;
    /**
     * 冲销SAP消息
     */
    private String revSapMsg;
    /**
     * 冲销SAP消息ID
     */
    private String revSapMessId;
    /**
     * 附件
     */
    private String fileObjId;
}

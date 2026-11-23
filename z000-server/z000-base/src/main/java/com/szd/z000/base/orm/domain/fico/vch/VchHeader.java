package com.szd.z000.base.orm.domain.fico.vch;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 费用报账-抬头
 */
@Data
public class VchHeader extends ClientWfBussBase {
    /**
     * 报账单ID
     */
    private String costId;
    /**
     * 申请金额
     */
    private BigDecimal amtApply;
    /**
     * SAP消息
     */
    private String sapMsg;
    /**
     * SAP消息ID
     */
    private String sapMessId;
    /**
     * SAP会计凭证编号
     */
    private String belnr;
    /**
     * SAP财年
     */
    private String gjahr;
    /**
     * 货币
     */
    private String curr;
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
}

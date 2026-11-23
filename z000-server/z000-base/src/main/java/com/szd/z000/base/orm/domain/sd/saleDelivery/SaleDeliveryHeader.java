package com.szd.z000.base.orm.domain.sd.saleDelivery;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import lombok.Data;

import java.util.Date;

/**
 * 销售交货单-抬头
 */
@Data
public class SaleDeliveryHeader extends ClientWfBussBase {
    /**
     * 交货单ID
     */
    private String deliveryId;
    /**
     * 装运点
     */
    private String shipPoint;
    /**
     * 截至交货到期日
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date deliveryDeadline;
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
     * SAP交货单号
     */
    private String vbeln;
    /**
     * SAP消息
     */
    private String sapMsg;
    /**
     * SAP消息ID
     */
    private String sapMessId;
}

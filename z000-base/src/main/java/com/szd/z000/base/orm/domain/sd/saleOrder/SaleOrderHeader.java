package com.szd.z000.base.orm.domain.sd.saleOrder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import lombok.Data;

import java.util.Date;

/**
 * 销售订单创建-抬头
 */
@Data
public class SaleOrderHeader extends ClientWfBussBase {
    /**
     * 销售订单ID
     */
    private String orderId;
    /**
     * 售达方客户编码
     */
    private String soldTo;
    /**
     * 送达方客户编码
     */
    private String shipTo;
    /**
     * 开票方客户编码
     */
    private String billTo;
    /**
     * 订单类型
     */
    private String orderType;
    /**
     * 销售机构
     */
    private String salesOrg;
    /**
     * 分销渠道
     */
    private String distrChan;
    /**
     * 产品组
     */
    // SAP字段: DIVISION
    private String prodGroup;
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
     * 售达方客户名称（非持久化）
     */
    private String soldToName;
    /**
     * 送达方客户名称（非持久化）
     */
    private String shipToName;
    /**
     * 开票方客户名称（非持久化）
     */
    private String billToName;
    /**
     * SAP销售订单号
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

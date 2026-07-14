package com.szd.z000.base.orm.domain.pp.orderCreate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 生产订单创建-抬头
 */
@Data
public class OrderCreateHeader extends ClientWfBussBase {
    /**
     * 生产订单创建ID
     */
    private String applyId;
    /**
     * 订单的物料编码
     */
    private String mtlCode;
    private String mtlName;
    /**
     * 工厂
     */
    private String fact;
    private String factName;
    /**
     * 订单类型
     */
    private String orderType;
    private String orderTypeName;
    /**
     * 基本开始日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startDate;
    /**
     * 基本完成日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 订单数量总计
     */
    private BigDecimal quantity;
    /**
     * 单位
     */
    private String unit;
    private String unitName;
    /**
     * 订单号(SAP返回)
     */
    private String aufnr;
    /**
     * 是否已下达(1/0)
     */
    private String released;
    private String releasedName;
    /**
     * 是否已关闭(1/0)
     */
    private String closed;
    private String closedName;
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
}

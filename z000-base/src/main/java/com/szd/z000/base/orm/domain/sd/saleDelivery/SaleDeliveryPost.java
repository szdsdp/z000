package com.szd.z000.base.orm.domain.sd.saleDelivery;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 销售交货单-过账信息
 */
@Data
public class SaleDeliveryPost {
    /**
     * 交货单ID
     */
    private String deliveryId;
    /**
     * 拣配日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date pickDate;
    /**
     * 过账日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date postDate;
    /**
     * 发货日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date shipDate;
    /**
     * 物料凭证号
     */
    private String mblnr;
    /**
     * 凭证年度
     */
    private String mjahr;
    /**
     * SAP消息
     */
    private String sapMsg;
    /**
     * SAP消息ID
     */
    private String sapMessId;
    /**
     * 冲销物料凭证
     */
    private String revMblnr;
    /**
     * 冲销凭证年度
     */
    private String revMjahr;
    /**
     * 冲销SAP消息
     */
    private String revSapMsg;
    /**
     * 冲销SAP消息ID
     */
    private String revSapMessId;
}

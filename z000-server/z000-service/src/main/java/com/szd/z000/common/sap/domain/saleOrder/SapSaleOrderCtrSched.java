package com.szd.z000.common.sap.domain.saleOrder;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

/**
 * 销售订单创建-交货计划
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
@Data
public class SapSaleOrderCtrSched {
    /**
     * 行号
     */
    @XmlElement(name = "ITM_NUMBER")
    private String itmNumber;
    /**
     * 计划到货日期
     */
    @XmlElement(name = "REQ_DATE")
    private String reqDate;
    /**
     * 数量
     */
    @XmlElement(name = "REQ_QTY")
    private BigDecimal reqQty;
}

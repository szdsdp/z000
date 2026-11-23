package com.szd.z000.common.sap.domain.orderReport;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

/**
 * 生产订单报工-行项目
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
@Data
public class SapOrderReportCtrItem {
    /** 活动编号（工序） */
    @XmlElement(name = "OPERATION")
    private String operation;
    /** 确认文本 */
    @XmlElement(name = "CONF_TEXT")
    private String confText;
    /** 待确认的产量 */
    @XmlElement(name = "YIELD")
    private BigDecimal yield;
    /** 当前要确认废品 */
    @XmlElement(name = "SCRAP")
    private BigDecimal scrap;
    /** 要确认的当前返工数量 */
    @XmlElement(name = "REWORK")
    private BigDecimal rework;
}

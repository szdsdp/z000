package com.szd.z000.common.sap.domain.saleOrder;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

/**
 * 销售订单创建-行项目
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
@Data
public class SapSaleOrderCtrItem {
    /**
     * 行号
     */
    @XmlElement(name = "ITM_NUMBER")
    private String itmNumber;
    /**
     * 物料编号
     */
    @XmlElement(name = "MATERIAL")
    private String material;
    /**
     * 数量
     */
    @XmlElement(name = "TARGET_QTY")
    private BigDecimal targetQty;
    /**
     * 单位
     */
    @XmlElement(name = "TARGET_QU")
    private String targetQu;
    /**
     * 行文本
     */
    @XmlElement(name = "SHORT_TEXT")
    private String shortText;
}

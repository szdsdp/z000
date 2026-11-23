package com.szd.z000.common.sap.domain.orderCreate;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 生产订单创建-抬头
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapOrderCreateCtrHeader {
    /** 单据号[外] */
    @XmlElement(name = "IF_DOCNO")
    private String ifDocno;
    /** 订单的物料编号 */
    @XmlElement(name = "MATERIAL")
    private String material;
    /** 工厂 */
    @XmlElement(name = "PLANT")
    private String plant;
    /** 订单类型 */
    @XmlElement(name = "ORDER_TYPE")
    private String orderType;
    /** 基本开始日期 */
    @XmlElement(name = "BASIC_START_DATE")
    private String basicStartDate;
    /** 基本完成日期 */
    @XmlElement(name = "BASIC_END_DATE")
    private String basicEndDate;
    /** 订单数量总计 */
    @XmlElement(name = "QUANTITY")
    private String quantity;
    /** 全部订单项通用单位 */
    @XmlElement(name = "QUANTITY_UOM")
    private String quantityUom;
}

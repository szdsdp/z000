package com.szd.z000.common.sap.domain.purStore;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

/**
 * 物料凭证创建-行项目
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
@Data
public class SapPurStoreCtrItem {
    /**
     * 物料编号
     */
    @XmlElement(name = "MATERIAL")
    private String material;
    /**
     * 工厂
     */
    @XmlElement(name = "PLANT")
    private String plant;
    /**
     * 库存地点
     */
    @XmlElement(name = "STGE_LOC")
    private String stgeLoc;
    /**
     * 移动类型(库存管理)
     */
    @XmlElement(name = "MOVE_TYPE")
    private String moveType;
    /**
     * 以输入单位计的数量
     */
    @XmlElement(name = "ENTRY_QNT")
    private BigDecimal entryQnt;
    /**
     * 条目单位
     */
    @XmlElement(name = "ENTRY_UOM")
    private String entryUom;
    /**
     * 采购订单编号
     */
    @XmlElement(name = "PO_NUMBER")
    private String poNumber;
    /**
     * 采购凭证的项目编号
     */
    @XmlElement(name = "PO_ITEM")
    private String poItem;
    /**
     * 项目文本
     */
    @XmlElement(name = "ITEM_TEXT")
    private String itemText;
    /**
     * 生产订单号
     */
    @XmlElement(name = "ORDERID")
    private String orderNo;
    /**
     * 生产订单号项目编号
     */
    @XmlElement(name = "ORDER_ITNO")
    private String orderItemNo;
}

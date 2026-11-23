package com.szd.z000.common.sap.domain.purOrder;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;

/**
 * 采购订单创建-行项目
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
@Data
public class SapPurOrderCtrItem {
    /**
     * 采购凭证的项目编号
     */
    @XmlElement(name = "PO_ITEM")
    private String poItem;
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
     * 采购订单数量
     */
    @XmlElement(name = "QUANTITY")
    private BigDecimal quantity;
    /**
     * 订单单位
     */
    @XmlElement(name = "PO_UNIT")
    private String poUnit;

    /**
     * 采购申请编号
     */
    @XmlElement(name = "PREQ_NO")
    private String preqNo;
    /**
     * 采购申请的项目编号
     */
    @XmlElement(name = "PREQ_ITEM")
    private String preqItem;
}

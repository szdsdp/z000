package com.szd.z000.common.sap.domain.purApply;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

/**
 * 采购申请创建-行项目
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
@Data
public class SapPurApplyCtrItem {
    /** 采购申请的项目编号 */
    @XmlElement(name = "PREQ_ITEM")
    private String preqItem;
    /** 采购组 */
    @XmlElement(name = "PUR_GROUP")
    private String purGroup;
    /** 需求者/请求者姓名 */
    @XmlElement(name = "PREQ_NAME")
    private String preqName;
    /** 短文本 */
    @XmlElement(name = "SHORT_TEXT")
    private String shortText;
    /** 物料编号 */
    @XmlElement(name = "MATERIAL")
    private String material;
    /** 工厂 */
    @XmlElement(name = "PLANT")
    private String plant;
    /** 库存地点 */
    @XmlElement(name = "STORE_LOC")
    private String storeLoc;
    /** 需求跟踪号 */
    @XmlElement(name = "TRACKINGNO")
    private String trackingno;
    /** 物料组 */
    @XmlElement(name = "MATL_GROUP")
    private String matlGroup;
    /** 采购申请数量 */
    @XmlElement(name = "QUANTITY")
    private BigDecimal quantity;
    /** 采购申请计量单位 */
    @XmlElement(name = "UNIT")
    private String unit;
    /** 需求（请求）日期 */
    @XmlElement(name = "PREQ_DATE")
    private String preqDate;
    /** 交货日期 */
    @XmlElement(name = "DELIV_DATE")
    private String delivDate;
}

package com.szd.z000.common.sap.domain.check.checkPlan;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

/**
 * 库存盘点创建-行项目
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
@Data
public class SapCheckPlanCtrItem {
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
     * 移动类型(701-盘盈/702-盘亏)
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
     * 项目文本
     */
    @XmlElement(name = "ITEM_TEXT")
    private String itemText;
}

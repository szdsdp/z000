package com.szd.z000.common.sap.domain.mtlcj;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 物料主数据创建/修改-采购视图
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "MARC")
@Data
public class SapMtlcjCtrMarc {
    /** 工厂 */
    @XmlElement(name = "PLANT")
    private String plant;
    /** 在工厂级标记要删除的物料 */
    @XmlElement(name = "DEL_FLAG")
    private String delFlag;
    /** ABC标识 */
    @XmlElement(name = "ABC_ID")
    private String abcId;
    /** 采购组 */
    @XmlElement(name = "PUR_GROUP")
    private String purGroup;
    /** 发货单位 */
    @XmlElement(name = "ISSUE_UNIT")
    private String issueUnit;
    /** 安全库存 */
    @XmlElement(name = "SAFETY_STK")
    private String safetyStk;
    /** 最小批量 */
    @XmlElement(name = "MINLOTSIZE")
    private String minlotsize;
    /** 最大批量大小 */
    @XmlElement(name = "MAXLOTSIZE")
    private String maxlotsize;
    /** 固定批量大小 */
    @XmlElement(name = "FIXED_LOT")
    private String fixedLot;
    /** 采购订单数量的舍入值 */
    @XmlElement(name = "ROUND_VAL")
    private String roundVal;
    /** 最大库存水平 */
    @XmlElement(name = "MAX_STOCK")
    private String maxStock;
}

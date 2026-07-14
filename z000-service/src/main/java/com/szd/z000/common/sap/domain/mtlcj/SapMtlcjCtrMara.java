package com.szd.z000.common.sap.domain.mtlcj;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 物料主数据创建/修改-基础视图
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "MARA")
@Data
public class SapMtlcjCtrMara {
    /** 基本计量单位 */
    @XmlElement(name = "BASE_UOM")
    private String baseUom;
    /** 物料组 */
    @XmlElement(name = "MATL_GROUP")
    private String matlGroup;
    /** 大小/量纲 */
    @XmlElement(name = "SIZE_DIM")
    private String sizeDim;
    /** 订单单位 */
    @XmlElement(name = "PO_UNIT")
    private String poUnit;
    /** 净重 */
    @XmlElement(name = "NET_WEIGHT")
    private String netWeight;
    /** 重量单位 */
    @XmlElement(name = "UNIT_OF_WT")
    private String unitOfWt;
}

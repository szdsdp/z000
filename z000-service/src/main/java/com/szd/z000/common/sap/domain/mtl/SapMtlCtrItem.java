package com.szd.z000.common.sap.domain.mtl;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 物料主数据同步-返回行项目
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapMtlCtrItem {
    /** 物料号 */
    @XmlElement(name = "MATNR")
    private String matnr;
    /** 物料描述 */
    @XmlElement(name = "MAKTX")
    private String maktx;
    /** 物料类型 */
    @XmlElement(name = "MTART")
    private String mtart;
    /** 基本单位 */
    @XmlElement(name = "MEINS")
    private String meins;
    /** 物料组 */
    @XmlElement(name = "MATKL")
    private String matkl;
    /** 产品组 */
    @XmlElement(name = "SPART")
    private String spart;
}

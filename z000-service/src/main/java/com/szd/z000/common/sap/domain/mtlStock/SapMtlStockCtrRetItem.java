package com.szd.z000.common.sap.domain.mtlStock;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 物料库存查询-返回行项目
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapMtlStockCtrRetItem {
    /** 物料编号 */
    @XmlElement(name = "MATNR")
    private String matnr;
    /** 物料描述 */
    @XmlElement(name = "MAKTX")
    private String maktx;
    /** 工厂 */
    @XmlElement(name = "WERKS")
    private String werks;
    /** 库存地点 */
    @XmlElement(name = "LGORT")
    private String lgort;
    /** 非限制库存 */
    @XmlElement(name = "LABST")
    private String labst;
    /** 冻结的库存 */
    @XmlElement(name = "SPEME")
    private String speme;
    /** 单位 */
    @XmlElement(name = "MEINS")
    private String meins;
}

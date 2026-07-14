package com.szd.z000.common.sap.domain.mtlStock;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 物料库存查询-请求抬头
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "HEADER")
@Data
public class SapMtlStockCtrHeader {
    /** 物料 */
    @XmlElement(name = "MATNR")
    private String matnr;
    /** 库位 */
    @XmlElement(name = "LGORT")
    private String lgort;
    /** 工厂 */
    @XmlElement(name = "WERKS")
    private String werks;
    /** 页码 */
    @XmlElement(name = "PAGENO")
    private String pageno;
    /** 页大小 */
    @XmlElement(name = "PAGESIZE")
    private String pagesize;
}

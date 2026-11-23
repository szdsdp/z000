package com.szd.z000.common.sap.domain.orderExtract;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 生产订单抽取-RESBS(组件)
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapOrderExtractCtrItemResbs {
    /** 预留编号 */
    @XmlElement(name = "RSNUM")
    private String rsnum;
    /** 预留项目 */
    @XmlElement(name = "RSPOS")
    private String rspos;
    /** 物料编号 */
    @XmlElement(name = "MATNR")
    private String matnr;
    /** 工厂 */
    @XmlElement(name = "WERKS")
    private String werks;
    /** 库存地点 */
    @XmlElement(name = "LGORT")
    private String lgort;
    /** 需求日期 */
    @XmlElement(name = "BDTER")
    private String bdter;
    /** 需求数量 */
    @XmlElement(name = "BDMNG")
    private String bdmng;
    /** 单位 */
    @XmlElement(name = "MEINS")
    private String meins;
}

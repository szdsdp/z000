package com.szd.z000.common.sap.domain.orderExtract;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 生产订单抽取-AFKO(订单头)
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapOrderExtractCtrItemAfko {
    /** 订单号 */
    @XmlElement(name = "AUFNR")
    private String aufnr;
    /** 订单类型 */
    @XmlElement(name = "AUART")
    private String auart;
    /** 订单描述 */
    @XmlElement(name = "KTEXT")
    private String ktext;
    /** 生产主管 */
    @XmlElement(name = "KOSTV")
    private String kostv;
    /** 基本完成日期 */
    @XmlElement(name = "GLTRP")
    private String gltrp;
    /** 订单开始日期 */
    @XmlElement(name = "GSTRP")
    private String gstrp;
    /** 预留编号 */
    @XmlElement(name = "RSNUM")
    private String rsnum;
    /** 已发货数量 */
    @XmlElement(name = "GASMG")
    private String gasmg;
    /** 订单数量 */
    @XmlElement(name = "GAMNG")
    private String gamng;
    /** 单位 */
    @XmlElement(name = "GMEIN")
    private String gmein;
    /** 物料编码 */
    @XmlElement(name = "MATNR")
    private String matnr;
    /** 工厂 */
    @XmlElement(name = "WERKS")
    private String werks;
    /** 工艺路线编号 */
    @XmlElement(name = "AUFPL")
    private String aufpl;
}

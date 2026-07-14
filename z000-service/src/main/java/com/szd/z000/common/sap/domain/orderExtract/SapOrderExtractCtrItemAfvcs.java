package com.szd.z000.common.sap.domain.orderExtract;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 生产订单抽取-AFVCS(工序)
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapOrderExtractCtrItemAfvcs {
    /**
     * 工艺路线编号
     */
    @XmlElement(name = "AUFPL")
    private String aufpl;
    /**
     * 工序内部编号
     */
    @XmlElement(name = "APLZL")
    private String aplzl;
    /**
     * 工序编号
     */
    @XmlElement(name = "VORNR")
    private String vornr;
    /**
     * 标准文本码
     */
    @XmlElement(name = "KTSCH")
    private String ktsch;
    /**
     * 工序描述
     */
    @XmlElement(name = "LTXA1")
    private String ltxa1;
}

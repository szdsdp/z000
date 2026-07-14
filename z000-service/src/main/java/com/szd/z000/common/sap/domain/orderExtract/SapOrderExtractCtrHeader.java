package com.szd.z000.common.sap.domain.orderExtract;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 生产订单抽取-请求抬头
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "HEADER")
@Data
public class SapOrderExtractCtrHeader {
    /** 开始日期 */
    @XmlElement(name = "BDATE")
    private String bdate;
    /** 结束日期 */
    @XmlElement(name = "EDATE")
    private String edate;
    /** 公司代码 */
    @XmlElement(name = "BUKRS")
    private String bukrs;
    /** 订单类型 */
    @XmlElement(name = "AUART")
    private String auart;
    /** 工厂 */
    @XmlElement(name = "WERKS")
    private String werks;
    /** 页码 */
    @XmlElement(name = "PAGENO")
    private String pageno;
    /** 每页大小 */
    @XmlElement(name = "PAGESIZE")
    private String pagesize;
}

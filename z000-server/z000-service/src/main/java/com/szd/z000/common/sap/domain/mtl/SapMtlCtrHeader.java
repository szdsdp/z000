package com.szd.z000.common.sap.domain.mtl;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 物料主数据同步-请求抬头
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "HEADER")
@Data
public class SapMtlCtrHeader {
    /** 开始日期 */
    @XmlElement(name = "BDATE")
    private String bdate;
    /** 结束日期 */
    @XmlElement(name = "EDATE")
    private String edate;
    /** 页码 */
    @XmlElement(name = "PAGENO")
    private String pageno;
    /** 页大小 */
    @XmlElement(name = "PAGESIZE")
    private String pagesize;
}

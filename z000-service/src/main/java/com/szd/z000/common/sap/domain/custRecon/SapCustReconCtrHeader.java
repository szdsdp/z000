package com.szd.z000.common.sap.domain.custRecon;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 客商对账查询-请求抬头
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "HEADER")
@Data
public class SapCustReconCtrHeader {
    /** 开始日期 */
    @XmlElement(name = "BDATE")
    private String bdate;
    /** 结束日期 */
    @XmlElement(name = "EDATE")
    private String edate;
    /** 公司代码 */
    @XmlElement(name = "BUKRS")
    private String bukrs;
    /** 客户 */
    @XmlElement(name = "KUNNR")
    private String kunnr;
    /** 页码 */
    @XmlElement(name = "PAGENO")
    private String pageno;
    /** 页大小 */
    @XmlElement(name = "PAGESIZE")
    private String pagesize;
}

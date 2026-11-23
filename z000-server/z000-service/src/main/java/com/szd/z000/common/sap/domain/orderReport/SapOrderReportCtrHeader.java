package com.szd.z000.common.sap.domain.orderReport;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 生产订单报工-抬头
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "HEADER")
@Data
public class SapOrderReportCtrHeader {
    /** 单据号[外] */
    @XmlElement(name = "IF_DOCNO")
    private String ifDocno;
    /** 报工类型 */
    @XmlElement(name = "ZTYPE")
    private String ztype;
    /** 订单号 */
    @XmlElement(name = "AUFNR")
    private String aufnr;
    /** 操作完成的确认编号 */
    @XmlElement(name = "CONFNO")
    private String confno;
    /** 确认计数器 */
    @XmlElement(name = "CONFCNT")
    private String confcnt;
    /** 凭证中的过帐日期 */
    @XmlElement(name = "BUDAT")
    private String budat;
}

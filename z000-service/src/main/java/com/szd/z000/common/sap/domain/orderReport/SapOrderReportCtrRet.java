package com.szd.z000.common.sap.domain.orderReport;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 生产订单报工-结果
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapOrderReportCtrRet {
    /**
     * 操作完成的确认编号
     */
    @XmlElement(name = "CONFNO")
    private String confno;
    /**
     * 确认计数器
     */
    @XmlElement(name = "CONFCNT")
    private String confcnt;
    /**
     * 状态(S 成功,E 错误)
     */
    @XmlElement(name = "ZSTAU")
    private String zstau;
    /**
     * 错误信息
     */
    @XmlElement(name = "ZMESS")
    private String zmess;
}

package com.szd.z000.common.sap.domain.saleOrder;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 销售订单创建-返回
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapSaleOrderCtrRet {
    /**
     * 单据号[外]
     */
    @XmlElement(name = "IF_DOCNO")
    private String ifDocno;
    /**
     * 销售订单号
     */
    @XmlElement(name = "VBELN")
    private String vbeln;
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

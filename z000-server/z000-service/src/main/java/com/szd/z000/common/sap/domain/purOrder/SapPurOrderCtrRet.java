package com.szd.z000.common.sap.domain.purOrder;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 采购订单创建-结果
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapPurOrderCtrRet {
    /**
     * 单据号[外]
     */
    @XmlElement(name = "IF_DOCNO")
    private String ifDocno;
    /**
     * 采购凭证号
     */
    @XmlElement(name = "EBELN")
    private String ebeln;
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

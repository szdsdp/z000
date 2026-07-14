package com.szd.z000.common.sap.domain.check.checkPlan;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 库存盘点创建-返回
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapCheckPlanCtrRet {
    @XmlElement(name = "IF_DOCNO")
    private String ifDocno;
    @XmlElement(name = "MBLNR")
    private String mblnr;
    @XmlElement(name = "MJAHR")
    private String mjahr;
    @XmlElement(name = "ZSTAU")
    private String zstau;
    @XmlElement(name = "ZMESS")
    private String zmess;
}

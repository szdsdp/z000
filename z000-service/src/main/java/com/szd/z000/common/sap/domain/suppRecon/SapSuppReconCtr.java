package com.szd.z000.common.sap.domain.suppRecon;

import lombok.Data;

import javax.xml.bind.annotation.*;

/**
 * 供应商对账查询-请求
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapSuppReconCtr {
    /** 抬头 */
    @XmlElement(name = "HEADER")
    private SapSuppReconCtrHeader header;
}

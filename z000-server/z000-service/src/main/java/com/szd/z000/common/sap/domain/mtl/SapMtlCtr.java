package com.szd.z000.common.sap.domain.mtl;

import lombok.Data;

import javax.xml.bind.annotation.*;

/**
 * 物料主数据同步-请求
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapMtlCtr {
    /** 抬头 */
    @XmlElement(name = "HEADER")
    private SapMtlCtrHeader header;
}

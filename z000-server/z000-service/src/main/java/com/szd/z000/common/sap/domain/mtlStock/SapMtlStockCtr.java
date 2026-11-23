package com.szd.z000.common.sap.domain.mtlStock;

import lombok.Data;

import javax.xml.bind.annotation.*;

/**
 * 物料库存查询-请求
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapMtlStockCtr {
    /** 抬头 */
    @XmlElement(name = "HEADER")
    private SapMtlStockCtrHeader header;
}

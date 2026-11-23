package com.szd.z000.common.sap.domain.suppRecon;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 供应商对账查询-返回
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapSuppReconCtrRet {
    /** 返回抬头 */
    @XmlElement(name = "HEADER")
    private SapSuppReconCtrRetHeader header;
    /** 对账明细清单 */
    @XmlElementWrapper(name = "ITEMS")
    @XmlElement(name = "item")
    private List<SapSuppReconCtrRetItem> items;
}

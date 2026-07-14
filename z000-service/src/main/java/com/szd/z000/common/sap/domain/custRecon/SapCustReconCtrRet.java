package com.szd.z000.common.sap.domain.custRecon;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 客商对账查询-返回
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapCustReconCtrRet {
    /** 返回抬头 */
    @XmlElement(name = "HEADER")
    private SapCustReconCtrRetHeader header;
    /** 对账明细清单 */
    @XmlElementWrapper(name = "ITEMS")
    @XmlElement(name = "item")
    private List<SapCustReconCtrRetItem> items;
}

package com.szd.z000.common.sap.domain.mtl;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 物料主数据同步-返回
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapMtlCtrRet {
    /** 返回抬头 */
    @XmlElement(name = "HEADER")
    private SapMtlCtrRetHeader header;
    /** 物料清单 */
    @XmlElementWrapper(name = "ITEMS")
    @XmlElement(name = "item")
    private List<SapMtlCtrItem> items;
}

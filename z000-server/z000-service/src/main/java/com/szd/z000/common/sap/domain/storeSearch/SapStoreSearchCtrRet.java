package com.szd.z000.common.sap.domain.storeSearch;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 物料凭证抽取-返回
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapStoreSearchCtrRet {
    /** 返回抬头 */
    @XmlElement(name = "HEADER")
    private SapStoreSearchCtrRetHeader header;
    /** 物料凭证清单 */
    @XmlElementWrapper(name = "ITEMS")
    @XmlElement(name = "item")
    private List<SapStoreSearchCtrRetItem> items;
}

package com.szd.z000.common.sap.domain.purApply;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 采购申请创建
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapPurApplyCtr {
    /**
     * 抬头
     */
    @XmlElement(name = "HEADER")
    private SapPurApplyCtrHeader header;
    /**
     * 行项目
     */
    @XmlElementWrapper(name = "ITEMS")
    @XmlElement(name = "item")
    private List<SapPurApplyCtrItem> itemList;
}

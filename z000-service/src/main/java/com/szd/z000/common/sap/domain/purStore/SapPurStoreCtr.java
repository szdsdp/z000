package com.szd.z000.common.sap.domain.purStore;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 物料凭证创建
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapPurStoreCtr {
    /**
     * 抬头
     */
    @XmlElement(name = "HEADER")
    private SapPurStoreCtrHeader header;
    /**
     * 行项目
     */
    @XmlElementWrapper(name = "ITEMS")
    @XmlElement(name = "item")
    private List<SapPurStoreCtrItem> itemList;
}

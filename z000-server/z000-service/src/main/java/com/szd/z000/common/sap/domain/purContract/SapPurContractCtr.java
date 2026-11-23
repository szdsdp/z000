package com.szd.z000.common.sap.domain.purContract;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 采购合同创建
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapPurContractCtr {
    /**
     * 抬头
     */
    @XmlElement(name = "HEADER")
    private SapPurContractCtrHeader header;
    /**
     * 行项目
     */
    @XmlElementWrapper(name = "ITEMS")
    @XmlElement(name = "item")
    private List<SapPurContractCtrItem> itemList;
}

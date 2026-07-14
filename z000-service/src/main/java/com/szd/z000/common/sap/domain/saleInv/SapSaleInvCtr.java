package com.szd.z000.common.sap.domain.saleInv;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 销售开票-请求
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapSaleInvCtr {
    /**
     * 抬头
     */
    @XmlElement(name = "HEADER")
    private SapSaleInvCtrHeader header;
    /**
     * 行项目
     */
    @XmlElementWrapper(name = "ITEMS")
    @XmlElement(name = "item")
    private List<SapSaleInvCtrItem> itemList;
}

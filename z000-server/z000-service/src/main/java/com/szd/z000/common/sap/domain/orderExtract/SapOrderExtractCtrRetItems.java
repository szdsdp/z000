package com.szd.z000.common.sap.domain.orderExtract;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 生产订单抽取-返回ITEMS
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapOrderExtractCtrRetItems {
    /** 数据行列表 */
    @XmlElement(name = "item")
    private List<SapOrderExtractCtrRetItem> itemList;
}

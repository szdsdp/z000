package com.szd.z000.common.sap.domain.orderExtract;

import lombok.Data;

import javax.xml.bind.annotation.*;

/**
 * 生产订单抽取-返回
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapOrderExtractCtrRet {
    /** 抬头 */
    @XmlElement(name = "HEADER")
    private SapOrderExtractCtrRetHeader header;
    /** 数据集 */
    @XmlElement(name = "ITEMS")
    private SapOrderExtractCtrRetItems items;
}

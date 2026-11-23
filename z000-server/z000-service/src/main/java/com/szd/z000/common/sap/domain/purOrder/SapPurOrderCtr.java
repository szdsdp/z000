package com.szd.z000.common.sap.domain.purOrder;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 采购订单创建
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapPurOrderCtr {
    /**
     * 抬头
     */
    @XmlElement(name = "HEADER")
    private SapPurOrderCtrHeader header;

    /**
     * 抬头文本
     */
    @XmlElement(name = "HEADTXTS")
    private SapPurOrderCtrHeadtxt headerTxt;
    /**
     * 行项目
     */
    @XmlElementWrapper(name = "ITEMS")
    @XmlElement(name = "item")
    private List<SapPurOrderCtrItem> itemList;
    /**
     * 交货计划
     */
    @XmlElementWrapper(name = "SCHEDS")
    @XmlElement(name = "sched")
    private List<SapPurOrderCtrSched> schedList;
}

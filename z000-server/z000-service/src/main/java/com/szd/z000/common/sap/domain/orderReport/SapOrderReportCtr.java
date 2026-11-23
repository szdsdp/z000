package com.szd.z000.common.sap.domain.orderReport;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 生产订单报工-请求
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapOrderReportCtr {
    /**
     * 抬头
     */
    @XmlElement(name = "HEADER")
    private SapOrderReportCtrHeader header;
    /**
     * 行项目
     */
    @XmlElementWrapper(name = "ITEMS")
    @XmlElement(name = "item")
    private List<SapOrderReportCtrItem> itemList;
}

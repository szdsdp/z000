package com.szd.z000.web.sap.pp.orderReport.domain;

import com.szd.z000.base.orm.domain.pp.orderReport.OrderReportHeader;
import com.szd.z000.base.orm.domain.pp.orderReport.OrderReportItem;
import lombok.Data;

import java.util.List;

/**
 * 生产订单报工-数据VO
 */
@Data
public class OrderReportDataVO {
    /**
     * 生产订单报工-抬头
     */
    private OrderReportHeader header;
    /**
     * 生产订单报工-行项目
     */
    private List<OrderReportItem> itemList;
}

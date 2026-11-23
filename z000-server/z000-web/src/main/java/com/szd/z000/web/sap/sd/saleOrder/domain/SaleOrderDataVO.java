package com.szd.z000.web.sap.sd.saleOrder.domain;

import com.szd.z000.base.orm.domain.sd.saleOrder.SaleOrderHeader;
import com.szd.z000.base.orm.domain.sd.saleOrder.SaleOrderItem;
import com.szd.z000.base.orm.domain.sd.saleOrder.SaleOrderSched;
import lombok.Data;

import java.util.List;

/**
 * 销售订单创建-数据VO
 */
@Data
public class SaleOrderDataVO {
    /**
     * 抬头
     */
    private SaleOrderHeader header;
    /**
     * 行项目
     */
    private List<SaleOrderItem> itemList;
    /**
     * 交货计划
     */
    private List<SaleOrderSched> schedList;
}

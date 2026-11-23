package com.szd.z000.web.sap.mm.purOrder.domain;

import com.szd.z000.base.orm.domain.mm.purOrder.PurOrderHeader;
import com.szd.z000.base.orm.domain.mm.purOrder.PurOrderItem;
import com.szd.z000.base.orm.domain.mm.purOrder.PurOrderRelateVo;
import com.szd.z000.base.orm.domain.mm.purOrder.PurOrderSched;
import lombok.Data;

import java.util.List;

/**
 * 采购订单-数据VO
 */
@Data
public class PurOrderDataVO {
    /**
     * 采购订单-抬头
     */
    private PurOrderHeader header;
    /**
     * 采购订单-行项目
     */
    private List<PurOrderItem> itemList;
    /**
     * 采购订单-交货计划
     */
    private List<PurOrderSched> schedList;
    /**
     * 关联单据列表
     */
    private List<PurOrderRelateVo> relateList;
}

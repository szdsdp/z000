package com.szd.z000.base.orm.mapper.sd.saleOrder;

import com.szd.z000.base.orm.domain.sd.saleOrder.SaleOrderSched;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 销售订单创建-交货计划Mapper
 */
public interface SaleOrderSchedMapper {
    List<SaleOrderSched> selectList(String orderId);
    void batchInsert(List<SaleOrderSched> list);
    void deleteByOrderId(String orderId);
    void deleteByOrderIdAndItemNo(@Param("orderId") String orderId, @Param("itemNo") String itemNo);
}

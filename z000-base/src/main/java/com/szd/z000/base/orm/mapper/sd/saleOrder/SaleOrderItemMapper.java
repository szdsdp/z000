package com.szd.z000.base.orm.mapper.sd.saleOrder;

import com.szd.z000.base.orm.domain.sd.saleOrder.SaleOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 销售订单创建-行项目Mapper
 */
public interface SaleOrderItemMapper {
    List<SaleOrderItem> selectList(String orderId);
    void batchInsert(List<SaleOrderItem> list);
    void deleteByOrderId(String orderId);
    void deleteByOrderIdAndItemNo(@Param("orderId") String orderId, @Param("itemNo") String itemNo);
}

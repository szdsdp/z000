package com.szd.z000.base.orm.mapper.mm.purOrder;

import com.szd.z000.base.orm.domain.mm.purOrder.PurOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购订单-行项目
 */
public interface PurOrderItemMapper {
    List<PurOrderItem> selectList(String orderId);
    List<PurOrderItem> selectByPreqNo(String applyNo);
    void batchInsert(List<PurOrderItem> list);
    void deleteByPurOrderId(String orderId);
    void deleteByPurOrderIdAndItemNo(@Param("orderId") String orderId, @Param("itemNo") String itemNo);
}

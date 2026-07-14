package com.szd.z000.base.orm.mapper.mm.purOrder;

import com.szd.z000.base.orm.domain.mm.purOrder.PurOrderSched;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购订单-交货计划
 */
public interface PurOrderSchedMapper {
    List<PurOrderSched> selectList(@Param("orderId") String orderId);
    void batchInsert(List<PurOrderSched> list);
    void deleteByPurOrderId(String orderId);
}

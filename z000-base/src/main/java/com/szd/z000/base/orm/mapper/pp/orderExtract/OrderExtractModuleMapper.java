package com.szd.z000.base.orm.mapper.pp.orderExtract;

import com.szd.z000.base.orm.domain.pp.orderExtract.OrderExtractModule;

import java.util.List;

/**
 * 生产订单抽取-组件Mapper
 */
public interface OrderExtractModuleMapper {
    List<OrderExtractModule> selectByOrderNo(String orderNo);
    void deleteByOrderNo(String orderNo);
    void batchInsert(List<OrderExtractModule> list);
}

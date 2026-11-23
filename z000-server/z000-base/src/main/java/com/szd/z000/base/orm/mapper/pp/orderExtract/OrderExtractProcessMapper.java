package com.szd.z000.base.orm.mapper.pp.orderExtract;

import com.szd.z000.base.orm.domain.pp.orderExtract.OrderExtractProcess;

import java.util.List;

/**
 * 生产订单抽取-工序Mapper
 */
public interface OrderExtractProcessMapper {
    List<OrderExtractProcess> selectByOrderNo(String orderNo);
    void deleteByOrderNo(String orderNo);
    void batchInsert(List<OrderExtractProcess> list);
}

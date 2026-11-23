package com.szd.z000.base.orm.mapper.pp.orderExtract;

import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.z000.base.orm.domain.pp.orderExtract.OrderExtractHeader;

import java.util.List;

/**
 * 生产订单抽取-订单头Mapper
 */
public interface OrderExtractHeaderMapper {
    List<OrderExtractHeader> selectList(ClientRpParam param);
    OrderExtractHeader selectById(String orderNo);
    void batchUpsert(List<OrderExtractHeader> list);
}

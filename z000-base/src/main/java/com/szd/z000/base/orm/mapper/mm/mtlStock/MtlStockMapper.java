package com.szd.z000.base.orm.mapper.mm.mtlStock;

import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.z000.base.orm.domain.mm.mtlStock.MtlStock;

import java.util.List;

/**
 * 库存查询-Mapper
 */
public interface MtlStockMapper {
    List<MtlStock> selectList(ClientRpParam param);
    void batchUpsert(List<MtlStock> list);
}

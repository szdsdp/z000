package com.szd.z000.base.orm.mapper.fico.vch;

import com.szd.z000.base.orm.domain.fico.vch.VchAdjust;

import java.util.List;

/**
 * 费用报账-凭证调整分录Mapper
 */
public interface VchAdjustMapper {
    List<VchAdjust> selectList(String costId);
    int batchInsert(List<VchAdjust> list);
    int deleteByCostId(String costId);
}

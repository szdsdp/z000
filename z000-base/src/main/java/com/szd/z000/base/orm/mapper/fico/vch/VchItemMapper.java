package com.szd.z000.base.orm.mapper.fico.vch;

import com.szd.z000.base.orm.domain.fico.vch.VchItem;

import java.util.List;

/**
 * 费用报账-行项目Mapper
 */
public interface VchItemMapper {
    List<VchItem> selectList(String costId);
    void batchInsert(List<VchItem> list);
    void deleteByCostId(String costId);
}

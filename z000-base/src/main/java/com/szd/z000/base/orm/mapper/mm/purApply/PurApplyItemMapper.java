package com.szd.z000.base.orm.mapper.mm.purApply;

import com.szd.z000.base.orm.domain.mm.purApply.PurApplyItem;

import java.util.List;

/**
 * 采购申请-行项目
 */
public interface PurApplyItemMapper {
    List<PurApplyItem> selectList(String applyId);
    void batchInsert(List<PurApplyItem> list);
    void deleteByApplyId(String applyId);
}

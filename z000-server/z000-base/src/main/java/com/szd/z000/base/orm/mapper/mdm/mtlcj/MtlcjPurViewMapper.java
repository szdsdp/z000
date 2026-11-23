package com.szd.z000.base.orm.mapper.mdm.mtlcj;

import com.szd.z000.base.orm.domain.mdm.mtlcj.MtlcjPurView;

/**
 * 物料主数据创建申请-采购视图
 */
public interface MtlcjPurViewMapper {
    MtlcjPurView selectByApplyId(String applyId);
    void insert(MtlcjPurView purView);
    void update(MtlcjPurView purView);
    void deleteByApplyId(String applyId);
}

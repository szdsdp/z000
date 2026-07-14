package com.szd.z000.base.orm.mapper.mdm.mtlcj;

import com.szd.z000.base.orm.domain.mdm.mtlcj.MtlcjBaseView;

/**
 * 物料主数据创建申请-基础视图
 */
public interface MtlcjBaseViewMapper {
    MtlcjBaseView selectByApplyId(String applyId);
    void insert(MtlcjBaseView baseView);
    void update(MtlcjBaseView baseView);
    void deleteByApplyId(String applyId);
}

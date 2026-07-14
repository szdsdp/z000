package com.szd.z000.base.orm.mapper.mm.check.checkPlan;

import com.szd.z000.base.orm.domain.mm.check.checkPlan.CheckPlanItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存盘点-行项目Mapper
 */
public interface CheckPlanItemMapper {
    List<CheckPlanItem> selectList(String planId);
    void batchInsert(List<CheckPlanItem> list);
    void deleteByPlanId(String planId);
    void deleteByPlanIdAndItemNo(@Param("planId") String planId, @Param("itemNo") String itemNo);
}

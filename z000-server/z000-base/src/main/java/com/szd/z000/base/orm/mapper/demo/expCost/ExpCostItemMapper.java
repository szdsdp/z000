package com.szd.z000.base.orm.mapper.demo.expCost;

import com.szd.z000.base.orm.domain.demo.expCost.ExpCostItem;

import java.util.List;

/**
 * 行项目Mapper
 *
 * @author duanlizeng
 */
public interface ExpCostItemMapper {
    List<ExpCostItem> selectList(String bussId);
    void batchInsert(List<ExpCostItem> list);
    void deleteByBussId(String bussId);
}

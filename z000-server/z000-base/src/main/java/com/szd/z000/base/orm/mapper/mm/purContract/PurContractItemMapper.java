package com.szd.z000.base.orm.mapper.mm.purContract;

import com.szd.z000.base.orm.domain.mm.purContract.PurContractItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购合同-行项目
 */
public interface PurContractItemMapper {
    List<PurContractItem> selectList(String contractId);
    void batchInsert(@Param("list") List<PurContractItem> itemList);
    void deleteByContractId(String contractId);
}

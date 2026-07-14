package com.szd.z000.base.orm.mapper.mdm.cust;

import com.szd.z000.base.orm.domain.mdm.cust.MdmCustPurchase;

import java.util.List;

/**
 * 客商申请-采购视图
 */
public interface MdmCustPurchaseMapper {
    List<MdmCustPurchase> selectList(String applyId);
    void batchInsert(List<MdmCustPurchase> list);
    void deleteById(String applyId);
}

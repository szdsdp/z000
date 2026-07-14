package com.szd.z000.base.orm.mapper.mdm.cust;

import com.szd.z000.base.orm.domain.mdm.cust.MdmCustSale;

import java.util.List;

/**
 * 客商申请-销售视图
 */
public interface MdmCustSaleMapper {
    List<MdmCustSale> selectList(String applyId);
    void batchInsert(List<MdmCustSale> list);
    void deleteById(String applyId);
}

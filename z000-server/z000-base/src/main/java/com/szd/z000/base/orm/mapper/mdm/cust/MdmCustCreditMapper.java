package com.szd.z000.base.orm.mapper.mdm.cust;

import com.szd.z000.base.orm.domain.mdm.cust.MdmCustCredit;

import java.util.List;

/**
 * 客商申请-信用视图
 */
public interface MdmCustCreditMapper {
    List<MdmCustCredit> selectList(String applyId);
    void batchInsert(List<MdmCustCredit> list);
    void deleteById(String applyId);
}

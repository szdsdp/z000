package com.szd.z000.base.orm.mapper.mdm.cust;

import com.szd.z000.base.orm.domain.mdm.cust.MdmCustBkac;

import java.util.List;

/**
 * 客商申请-银行视图
 */
public interface MdmCustBkacMapper {
    List<MdmCustBkac> selectList(String applyId);
    void batchInsert(List<MdmCustBkac> list);
    void deleteById(String applyId);
}

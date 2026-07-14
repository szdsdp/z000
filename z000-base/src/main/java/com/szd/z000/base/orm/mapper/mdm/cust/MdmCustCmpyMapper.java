package com.szd.z000.base.orm.mapper.mdm.cust;

import com.szd.z000.base.orm.domain.mdm.cust.MdmCustCmpy;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客商申请-公司视图
 */
public interface MdmCustCmpyMapper {
    List<MdmCustCmpy> selectList(String applyId);
    void batchInsert(List<MdmCustCmpy> list);
    void deleteByApplyId(String applyId);
    void deleteByApplyIdAndFlag(@Param("applyId") String applyId, @Param("custFlag") String custFlag);
}

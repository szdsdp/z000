package com.szd.z000.base.orm.mapper.mdm.cust;

import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.z000.base.orm.domain.mdm.cust.MdmCustHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客商申请-抬头
 */
public interface MdmCustHeaderMapper {
    List<MdmCustHeader> selectList(ClientRpParam param);
    MdmCustHeader selectById(String applyId);
    void insert(MdmCustHeader mdmCustHeader);
    void update(MdmCustHeader mdmCustHeader);
    void updateCustId(@Param("applyId") String applyId, @Param("custId") String custId, @Param("sapMsg") String sapMsg, @Param("sapMessId") String sapMessId);
    void updateStatusAndDocId(@Param("applyId") String applyId, @Param("bussStatus") String bussStatus, @Param("bussDocId") String bussDocId);
    void deleteById(String applyId);
}

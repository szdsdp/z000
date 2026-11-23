package com.szd.z000.base.orm.mapper.mm.check.checkPlan;

import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.z000.base.orm.domain.mm.check.checkPlan.CheckPlanHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存盘点-抬头Mapper
 */
public interface CheckPlanHeaderMapper {
    List<CheckPlanHeader> selectList(ClientRpParam param);
    CheckPlanHeader selectById(String planId);
    void insert(CheckPlanHeader header);
    void update(CheckPlanHeader header);
    void updateStatusAndDocId(@Param("planId") String planId, @Param("bussStatus") String bussStatus, @Param("bussDocId") String bussDocId);
    void updateSapRet(@Param("planId") String planId, @Param("mblnr") String mblnr, @Param("mjahr") String mjahr,
                      @Param("sapMsg") String sapMsg, @Param("sapMessId") String sapMessId);
    void updateReverseSapRet(@Param("planId") String planId, @Param("revMblnr") String revMblnr,
                             @Param("revMjahr") String revMjahr, @Param("revSapMsg") String revSapMsg,
                             @Param("revSapMessId") String revSapMessId);
    void deleteById(String planId);
    void updatePlanStatus(@Param("planId") String planId, @Param("planStatus") String planStatus);
    void updateBussStatus(@Param("planId") String planId, @Param("bussStatus") String bussStatus);
}

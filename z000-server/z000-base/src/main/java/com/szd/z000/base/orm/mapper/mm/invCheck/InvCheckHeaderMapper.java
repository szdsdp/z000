package com.szd.z000.base.orm.mapper.mm.invCheck;

import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.z000.base.orm.domain.mm.invCheck.InvCheckHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 发票校验-抬头
 */
public interface InvCheckHeaderMapper {
    List<InvCheckHeader> selectList(ClientRpParam param);
    InvCheckHeader selectById(String invId);
    void insert(InvCheckHeader header);
    void update(InvCheckHeader header);
    void updateStatusAndDocId(@Param("invId") String invId, @Param("bussStatus") String bussStatus, @Param("bussDocId") String bussDocId);
    void updateSapRet(@Param("invId") String invId, @Param("reBelnr") String reBelnr, @Param("reGjahr") String reGjahr,
                      @Param("bukrs") String bukrs, @Param("belnr") String belnr, @Param("gjahr") String gjahr,
                      @Param("sapMsg") String sapMsg, @Param("sapMessId") String sapMessId);
    void updateReverseSapRet(@Param("invId") String invId, @Param("revReBelnr") String revReBelnr,
                             @Param("revReGjahr") String revReGjahr, @Param("revBelnr") String revBelnr,
                             @Param("revGjahr") String revGjahr, @Param("revSapMsg") String revSapMsg,
                             @Param("revSapMessId") String revSapMessId);
    List<InvCheckHeader> selectByStoreBussDocId(String storeBussDocId);
    int countByStoreBussDocId(@Param("storeBussDocId") String storeBussDocId, @Param("bussStatus") String bussStatus);
    java.math.BigDecimal sumAmtByStoreBussDocId(@Param("storeBussDocId") String storeBussDocId, @Param("bussStatus") String bussStatus);
    void deleteById(String invId);
}

package com.szd.z000.base.orm.mapper.mm.purStore;

import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.z000.base.orm.domain.mm.purStore.PurStoreHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物料凭证-抬头
 */
public interface PurStoreHeaderMapper {
    List<PurStoreHeader> selectList(ClientRpParam param);
    PurStoreHeader selectById(String storeId);
    void insert(PurStoreHeader header);
    void update(PurStoreHeader header);
    void updateStatusAndDocId(@Param("storeId") String storeId, @Param("bussStatus") String bussStatus, @Param("bussDocId") String bussDocId);
    void updateSapRet(@Param("storeId") String storeId, @Param("mblnr") String mblnr, @Param("mjahr") String mjahr,
                      @Param("sapMsg") String sapMsg, @Param("sapMessId") String sapMessId);
    void updateReverseSapRet(@Param("storeId") String storeId, @Param("revMblnr") String revMblnr,
                             @Param("revMjahr") String revMjahr, @Param("revSapMsg") String revSapMsg,
                             @Param("revSapMessId")  String revSapMessId);
    void deleteById(String storeId);
}

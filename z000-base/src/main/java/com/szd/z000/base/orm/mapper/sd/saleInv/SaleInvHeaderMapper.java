package com.szd.z000.base.orm.mapper.sd.saleInv;

import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.z000.base.orm.domain.sd.saleInv.SaleInvHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 销售开票-抬头Mapper
 */
public interface SaleInvHeaderMapper {
    List<SaleInvHeader> selectList(ClientRpParam param);
    SaleInvHeader selectById(String invId);
    void insert(SaleInvHeader header);
    void update(SaleInvHeader header);
    void updateStatusAndDocId(@Param("invId") String invId, @Param("bussStatus") String bussStatus, @Param("bussDocId") String bussDocId);
    void updateSapRet(@Param("invId") String invId, @Param("billDoc") String billDoc,
                      @Param("sapMsg") String sapMsg, @Param("sapMessId") String sapMessId);
    void updateReverseSapRet(@Param("invId") String invId, @Param("revBillDoc") String revBillDoc,
                             @Param("revSapMsg") String revSapMsg, @Param("revSapMessId") String revSapMessId);
    void deleteById(String invId);
}

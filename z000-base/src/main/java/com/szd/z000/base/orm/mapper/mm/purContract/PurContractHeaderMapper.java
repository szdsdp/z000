package com.szd.z000.base.orm.mapper.mm.purContract;

import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.z000.base.orm.domain.mm.purContract.PurContractHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购合同-抬头
 */
public interface PurContractHeaderMapper {
    List<PurContractHeader> selectList(ClientRpParam param);
    PurContractHeader selectById(String contractId);
    void insert(PurContractHeader header);
    void update(PurContractHeader header);
    void updateStatusAndDocId(@Param("contractId") String contractId, @Param("bussStatus") String bussStatus, @Param("bussDocId") String bussDocId);
    void updatePurCont(@Param("contractId") String contractId, @Param("contractNo") String contractNo, @Param("sapMsg") String sapMsg, @Param("sapMessId") String sapMessId);
    void deleteById(String contractId);
}

package com.szd.z000.base.orm.mapper.fico.vch;

import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.z000.base.orm.domain.fico.vch.VchHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 费用报账-抬头Mapper
 */
public interface VchHeaderMapper {
    List<VchHeader> selectList(ClientRpParam param);
    VchHeader selectById(String costId);
    void insert(VchHeader header);
    void update(VchHeader header);
    void updateStatusAndDocId(@Param("costId") String costId, @Param("bussStatus") String bussStatus, @Param("bussDocId") String bussDocId);
    void updateSapInfo(@Param("costId") String costId, @Param("belnr") String belnr, @Param("gjahr") String gjahr, @Param("sapMsg") String sapMsg, @Param("sapMessId") String sapMessId);
    void deleteById(String costId);
}

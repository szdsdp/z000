package com.szd.z000.base.orm.mapper.mdm.mtlcj;

import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.z000.base.orm.domain.mdm.mtlcj.MtlcjHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物料主数据创建申请-基本信息
 */
public interface MtlcjHeaderMapper {
    List<MtlcjHeader> selectList(ClientRpParam param);
    MtlcjHeader selectById(String applyId);
    void insert(MtlcjHeader header);
    void update(MtlcjHeader header);
    void updateStatusAndDocId(@Param("applyId") String applyId, @Param("bussStatus") String bussStatus, @Param("bussDocId") String bussDocId);
    void updateMtlCode(@Param("applyId") String applyId, @Param("mtlCode") String mtlCode, @Param("sapMsg") String sapMsg, @Param("sapMessId") String sapMessId);
    void deleteById(String applyId);
}

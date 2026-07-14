package com.szd.z000.base.orm.mapper.mm.purApply;

import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.z000.base.orm.domain.mm.purApply.PurApplyHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购申请-抬头
 */
public interface PurApplyHeaderMapper {
    List<PurApplyHeader> selectList(ClientRpParam param);
    PurApplyHeader selectById(String applyId);
    void insert(PurApplyHeader header);
    void update(PurApplyHeader header);
    void updateStatusAndDocId(@Param("applyId") String applyId, @Param("bussStatus") String bussStatus, @Param("bussDocId") String bussDocId);
    void updateApplyNo(@Param("applyId") String applyId, @Param("applyNo") String applyNo, @Param("sapMsg") String sapMsg, @Param("sapMessId") String sapMessId);
    void deleteById(String applyId);
}

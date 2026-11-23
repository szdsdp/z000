package com.szd.z000.base.orm.mapper.mdm.mtl;

import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.z000.base.orm.domain.mdm.mtl.Mtl;

import java.util.List;

/**
 * 物料主数据-Mapper
 */
public interface MtlMapper {
    List<Mtl> selectList(ClientRpParam param);
    List<Mtl> selectByCodes(List<String> mtlCodes);
    void batchUpsert(List<Mtl> list);
}

package com.szd.z000.base.orm.mapper.demo.expCost;

import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.z000.base.orm.domain.demo.expCost.ExpCostHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 抬头表Mapper
 *
 * @author duanlizeng
 */
public interface ExpCostHeaderMapper {
    List<ExpCostHeader> selectList(ClientRpParam param);
    ExpCostHeader selectById(String bussId);
    void insert(ExpCostHeader header);
    void update(ExpCostHeader header);
    void updateStatusAndDocId(@Param("bussId") String bussId, @Param("bussStatus") String bussStatus, @Param("bussDocId") String bussDocId);
    void deleteById(String bussId);
}

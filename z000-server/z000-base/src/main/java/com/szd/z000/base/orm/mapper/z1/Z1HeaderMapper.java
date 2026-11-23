package com.szd.z000.base.orm.mapper.z1;

import com.szd.z000.base.orm.domain.z1.Z1Header;
import com.szd.z000.base.orm.domain.z1.Z1RpParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * SDEM1-抬头
 *
 * @author ADMIN
 */
public interface Z1HeaderMapper {
    List<Z1Header> selectList(Z1RpParam param);
    Z1Header selectById(String bussId);
    int insert(Z1Header header);
    int update(Z1Header header);
    int deleteById(String bussId);
    void updateStatusAndDocId(@Param("bussId") String bussId, @Param("bussStatus") String bussStatus, @Param("bussDocId") String bussDocId);
}

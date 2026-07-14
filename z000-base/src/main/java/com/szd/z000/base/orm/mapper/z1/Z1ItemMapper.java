package com.szd.z000.base.orm.mapper.z1;

import com.szd.z000.base.orm.domain.z1.Z1Item;
import java.util.List;

/**
 * SDEM1-行项目
 *
 * @author ADMIN
 */
public interface Z1ItemMapper {
    List<Z1Item> selectList(String bussId);
    int batchInsert(List<Z1Item> list);
    int deleteByBussId(String bussId);
}

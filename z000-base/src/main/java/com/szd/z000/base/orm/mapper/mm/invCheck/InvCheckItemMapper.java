package com.szd.z000.base.orm.mapper.mm.invCheck;

import com.szd.z000.base.orm.domain.mm.invCheck.InvCheckItem;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 发票校验-行项目
 */
public interface InvCheckItemMapper {
    List<InvCheckItem> selectList(String invId);
    void batchInsert(List<InvCheckItem> list);
    void deleteByInvCheckId(String invId);
    BigDecimal countByPoNumberAndPoItem(@Param("orderNo") String orderNo, @Param("orderItemNo") String orderItemNo, @Param("excludeInvId") String excludeInvId);
}

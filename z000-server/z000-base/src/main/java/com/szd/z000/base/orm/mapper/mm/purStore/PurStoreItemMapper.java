package com.szd.z000.base.orm.mapper.mm.purStore;

import com.szd.z000.base.orm.domain.mm.purStore.PurStoreItem;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 物料凭证-行项目
 */
public interface PurStoreItemMapper {
    List<PurStoreItem> selectList(String storeId);
    void batchInsert(List<PurStoreItem> list);
    void deleteByPurStoreId(String storeId);
    void deleteByPurStoreIdAndItemNo(@Param("storeId") String storeId, @Param("itemNo") String itemNo);
    int countByOrderNoAndOrderItemNo(@Param("orderNo") String orderNo, @Param("orderItemNo") String orderItemNo);
    BigDecimal countValidByOrderNoAndOrderItemNo(@Param("orderNo") String orderNo, @Param("orderItemNo") String orderItemNo);
    List<String> selectStoreIdsByRefDoc(@Param("refBussDocId") String refBussDocId, @Param("refBstp") String refBstp);
    int countStoreByRefDoc(@Param("refBussDocId") String refBussDocId, @Param("refBstp") String refBstp, @Param("bussStatus") String bussStatus);
}

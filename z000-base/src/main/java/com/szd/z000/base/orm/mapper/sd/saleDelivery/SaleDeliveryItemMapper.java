package com.szd.z000.base.orm.mapper.sd.saleDelivery;

import com.szd.z000.base.orm.domain.sd.saleDelivery.SaleDeliveryItem;
import java.math.BigDecimal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 销售交货单-行项目Mapper
 */
public interface SaleDeliveryItemMapper {
    List<SaleDeliveryItem> selectList(String deliveryId);
    void batchInsert(List<SaleDeliveryItem> list);
    void deleteByDeliveryId(String deliveryId);
    void deleteByDeliveryIdAndItemNo(@Param("deliveryId") String deliveryId, @Param("itemNo") String itemNo);
    BigDecimal sumDlvQtyByOrderNoAndOrderItemNo(@Param("orderNo") String orderNo, @Param("orderItemNo") String orderItemNo, @Param("excludeDeliveryId") String excludeDeliveryId);
}

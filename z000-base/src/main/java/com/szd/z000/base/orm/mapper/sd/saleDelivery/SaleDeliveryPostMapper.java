package com.szd.z000.base.orm.mapper.sd.saleDelivery;

import com.szd.z000.base.orm.domain.sd.saleDelivery.SaleDeliveryPost;
import org.apache.ibatis.annotations.Param;

/**
 * 销售交货单-过账信息Mapper
 */
public interface SaleDeliveryPostMapper {
    SaleDeliveryPost selectById(String deliveryId);
    void insert(SaleDeliveryPost post);
    void update(SaleDeliveryPost post);
    void updateSapRet(@Param("deliveryId") String deliveryId, @Param("mblnr") String mblnr, @Param("mjahr") String mjahr,
                      @Param("sapMsg") String sapMsg, @Param("sapMessId") String sapMessId);
    void updateReverseSapRet(@Param("deliveryId") String deliveryId, @Param("revMblnr") String revMblnr,
                             @Param("revMjahr") String revMjahr, @Param("revSapMsg") String revSapMsg,
                             @Param("revSapMessId") String revSapMessId);
}

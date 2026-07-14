package com.szd.z000.base.orm.mapper.sd.saleDelivery;

import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.z000.base.orm.domain.sd.saleDelivery.SaleDeliveryHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 销售交货单-抬头Mapper
 */
public interface SaleDeliveryHeaderMapper {
    List<SaleDeliveryHeader> selectList(ClientRpParam param);
    SaleDeliveryHeader selectById(String deliveryId);
    void insert(SaleDeliveryHeader header);
    void update(SaleDeliveryHeader header);
    void updateStatusAndDocId(@Param("deliveryId") String deliveryId, @Param("bussStatus") String bussStatus, @Param("bussDocId") String bussDocId);
    void updateSapRet(@Param("deliveryId") String deliveryId, @Param("vbeln") String vbeln,
                      @Param("sapMsg") String sapMsg, @Param("sapMessId") String sapMessId);
    /**
     * 根据SAP交货单号查询
     */
    SaleDeliveryHeader selectByVbeln(String vbeln);

    void deleteById(String deliveryId);
}

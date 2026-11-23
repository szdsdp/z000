package com.szd.z000.base.orm.mapper.sd.saleOrder;

import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.z000.base.orm.domain.sd.saleOrder.SaleOrderHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 销售订单创建-抬头Mapper
 */
public interface SaleOrderHeaderMapper {
    List<SaleOrderHeader> selectList(ClientRpParam param);
    SaleOrderHeader selectById(String orderId);
    void insert(SaleOrderHeader header);
    void update(SaleOrderHeader header);
    void updateStatusAndDocId(@Param("orderId") String orderId, @Param("bussStatus") String bussStatus, @Param("bussDocId") String bussDocId);
    void updateSapRet(@Param("orderId") String orderId, @Param("vbeln") String vbeln,
                      @Param("sapMsg") String sapMsg, @Param("sapMessId") String sapMessId);
    void deleteById(String orderId);
}

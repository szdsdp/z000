package com.szd.z000.base.orm.mapper.mm.purOrder;

import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.z000.base.orm.domain.mm.purOrder.PurOrderHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购订单-抬头
 */
public interface PurOrderHeaderMapper {
    List<PurOrderHeader> selectList(ClientRpParam param);
    PurOrderHeader selectById(String orderId);
    PurOrderHeader selectByApplyId(String applyId);
    void insert(PurOrderHeader header);
    void update(PurOrderHeader header);
    void updateStatusAndDocId(@Param("orderId") String orderId, @Param("bussStatus") String bussStatus, @Param("bussDocId") String bussDocId);
    void updateCgpzh(@Param("orderId") String orderId, @Param("vchNo") String vchNo, @Param("sapMsg") String sapMsg, @Param("sapMessId") String sapMessId);
    void deleteById(String orderId);
}

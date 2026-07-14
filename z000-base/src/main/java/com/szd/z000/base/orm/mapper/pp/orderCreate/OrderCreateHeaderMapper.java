package com.szd.z000.base.orm.mapper.pp.orderCreate;

import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.z000.base.orm.domain.pp.orderCreate.OrderCreateHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 生产订单创建-抬头
 */
public interface OrderCreateHeaderMapper {
    List<OrderCreateHeader> selectList(ClientRpParam param);

    OrderCreateHeader selectById(String applyId);

    void insert(OrderCreateHeader header);

    void update(OrderCreateHeader header);

    void updateStatusAndDocId(@Param("applyId") String applyId, @Param("bussStatus") String bussStatus, @Param("bussDocId") String bussDocId);

    void updateSapRet(@Param("applyId") String applyId, @Param("aufnr") String aufnr,
                      @Param("sapMsg") String sapMsg, @Param("sapMessId") String sapMessId);

    void updateReleased(@Param("applyId") String applyId, @Param("released") String released);

    void updateClosed(@Param("applyId") String applyId, @Param("closed") String closed);

    void deleteById(String applyId);
}

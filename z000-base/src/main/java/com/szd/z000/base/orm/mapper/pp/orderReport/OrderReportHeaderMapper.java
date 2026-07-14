package com.szd.z000.base.orm.mapper.pp.orderReport;

import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.z000.base.orm.domain.pp.orderReport.OrderReportHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 生产订单报工-抬头
 */
public interface OrderReportHeaderMapper {
    List<OrderReportHeader> selectList(ClientRpParam param);
    OrderReportHeader selectById(String reportId);
    void insert(OrderReportHeader header);
    void update(OrderReportHeader header);
    void updateStatusAndDocId(@Param("reportId") String reportId, @Param("bussStatus") String bussStatus, @Param("bussDocId") String bussDocId);
    void updateSapRet(@Param("reportId") String reportId, @Param("confno") String confno, @Param("confcnt") String confcnt,
                      @Param("sapMsg") String sapMsg, @Param("sapMessId") String sapMessId);
    void updateReverseSapRet(@Param("reportId") String reportId, @Param("revConfcnt") String revConfcnt,
                             @Param("revSapMsg") String revSapMsg, @Param("revSapMessId") String revSapMessId);
    void deleteById(String reportId);
}

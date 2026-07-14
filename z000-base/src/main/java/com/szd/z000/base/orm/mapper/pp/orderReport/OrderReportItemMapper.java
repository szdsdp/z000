package com.szd.z000.base.orm.mapper.pp.orderReport;

import com.szd.z000.base.orm.domain.pp.orderReport.OrderReportItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 生产订单报工-行项目
 */
public interface OrderReportItemMapper {
    List<OrderReportItem> selectList(String reportId);
    void batchInsert(List<OrderReportItem> list);
    void deleteByOrderReportId(String reportId);
    void deleteByOrderReportIdAndItemNo(@Param("reportId") String reportId, @Param("itemNo") String itemNo);
}

package com.szd.z000.web.sap.pp.orderReport.service;

import com.szd.z000.base.orm.domain.pp.orderReport.OrderReportItem;
import com.szd.z000.base.orm.mapper.pp.orderReport.OrderReportItemMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 生产订单报工-行项目服务
 */
@Service
public class OrderReportItemService {
    @Resource
    private OrderReportItemMapper orderReportItemMapper;

    public List<OrderReportItem> selectList(String reportId) {
        List<OrderReportItem> list = orderReportItemMapper.selectList(reportId);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public void save(String reportId, List<OrderReportItem> itemList) {
        orderReportItemMapper.deleteByOrderReportId(reportId);
        if (!CollectionUtils.isEmpty(itemList)) {
            for (int i = 0; i < itemList.size(); i++) {
                OrderReportItem item = itemList.get(i);
                item.setReportId(reportId);
            }
            orderReportItemMapper.batchInsert(itemList);
        }
    }

    public void deleteByOrderReportId(String reportId) {
        orderReportItemMapper.deleteByOrderReportId(reportId);
    }
}

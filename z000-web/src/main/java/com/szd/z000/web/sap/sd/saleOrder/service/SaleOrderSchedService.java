package com.szd.z000.web.sap.sd.saleOrder.service;

import com.szd.z000.base.orm.domain.sd.saleOrder.SaleOrderSched;
import com.szd.z000.base.orm.mapper.sd.saleOrder.SaleOrderSchedMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 销售订单创建-交货计划服务
 */
@Service
public class SaleOrderSchedService {
    @Resource
    private SaleOrderSchedMapper saleOrderSchedMapper;

    /**
     * 根据销售订单ID查询交货计划列表
     */
    public List<SaleOrderSched> selectList(String orderId) {
        List<SaleOrderSched> list = saleOrderSchedMapper.selectList(orderId);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    /**
     * 保存交货计划     */
    public void save(String orderId, List<SaleOrderSched> schedList) {
        saleOrderSchedMapper.deleteByOrderId(orderId);
        if (!CollectionUtils.isEmpty(schedList)) {
            for (int i = 0; i < schedList.size(); i++) {
                SaleOrderSched sched = schedList.get(i);
                sched.setOrderId(orderId);
            }
            saleOrderSchedMapper.batchInsert(schedList);
        }
    }

    /**
     * 根据销售订单ID删除所有交货计划
     */
    public void deleteByOrderId(String orderId) {
        saleOrderSchedMapper.deleteByOrderId(orderId);
    }
}

package com.szd.z000.web.sap.mm.purOrder.service;

import com.szd.z000.base.orm.domain.mm.purOrder.PurOrderSched;
import com.szd.z000.base.orm.mapper.mm.purOrder.PurOrderSchedMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 采购订单-交货计划服务
 */
@Service
public class PurOrderSchedService {
    @Resource
    private PurOrderSchedMapper purOrderSchedMapper;

    public List<PurOrderSched> selectList(String orderId) {
        List<PurOrderSched> list = purOrderSchedMapper.selectList(orderId);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public void save(String orderId, List<PurOrderSched> schedList) {
        purOrderSchedMapper.deleteByPurOrderId(orderId);
        if (!CollectionUtils.isEmpty(schedList)) {
            for (int i = 0; i < schedList.size(); i++) {
                PurOrderSched sched = schedList.get(i);
                sched.setOrderId(orderId);
            }
            purOrderSchedMapper.batchInsert(schedList);
        }
    }

    public void deleteByPurOrderId(String orderId) {
        purOrderSchedMapper.deleteByPurOrderId(orderId);
    }
}

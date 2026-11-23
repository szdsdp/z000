package com.szd.z000.web.sap.mm.purOrder.service;

import com.szd.z000.base.orm.domain.mm.purOrder.PurOrderItem;
import com.szd.z000.base.orm.mapper.mm.purOrder.PurOrderItemMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 采购订单-行项目服务
 */
@Service
public class PurOrderItemService {
    @Resource
    private PurOrderItemMapper purOrderItemMapper;

    public List<PurOrderItem> selectList(String orderId) {
        List<PurOrderItem> list = purOrderItemMapper.selectList(orderId);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public void save(String orderId, List<PurOrderItem> itemList) {
        purOrderItemMapper.deleteByPurOrderId(orderId);
        if (!CollectionUtils.isEmpty(itemList)) {
            for (int i = 0; i < itemList.size(); i++) {
                PurOrderItem item = itemList.get(i);
                item.setOrderId(orderId);
            }
            purOrderItemMapper.batchInsert(itemList);
        }
    }

    public List<PurOrderItem> selectByPreqNo(String applyNo) {
        List<PurOrderItem> list = purOrderItemMapper.selectByPreqNo(applyNo);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public void deleteByPurOrderId(String orderId) {
        purOrderItemMapper.deleteByPurOrderId(orderId);
    }
}

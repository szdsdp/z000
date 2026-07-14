package com.szd.z000.web.sap.sd.saleOrder.service;

import com.szd.z000.base.orm.domain.sd.saleOrder.SaleOrderItem;
import com.szd.z000.base.orm.mapper.sd.saleOrder.SaleOrderItemMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 销售订单创建-行项目服务
 */
@Service
public class SaleOrderItemService {
    @Resource
    private SaleOrderItemMapper saleOrderItemMapper;

    /**
     * 根据销售订单ID查询行项目列表
     */
    public List<SaleOrderItem> selectList(String orderId) {
        List<SaleOrderItem> list = saleOrderItemMapper.selectList(orderId);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    /**
     * 保存行项目     */
    public void save(String orderId, List<SaleOrderItem> itemList) {
        saleOrderItemMapper.deleteByOrderId(orderId);
        if (!CollectionUtils.isEmpty(itemList)) {
            for (int i = 0; i < itemList.size(); i++) {
                SaleOrderItem item = itemList.get(i);
                item.setOrderId(orderId);
            }
            saleOrderItemMapper.batchInsert(itemList);
        }
    }

    /**
     * 根据销售订单ID删除所有行项目
     */
    public void deleteByOrderId(String orderId) {
        saleOrderItemMapper.deleteByOrderId(orderId);
    }
}

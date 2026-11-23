package com.szd.z000.web.sap.sd.saleDelivery.service;

import com.szd.z000.base.orm.domain.sd.saleDelivery.SaleDeliveryItem;
import com.szd.z000.base.orm.mapper.sd.saleDelivery.SaleDeliveryItemMapper;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 销售交货单-行项目服务
 */
@Service
public class SaleDeliveryItemService {
    @Resource
    private SaleDeliveryItemMapper saleDeliveryItemMapper;

    /**
     * 根据交货单ID查询行项目列表
     */
    public List<SaleDeliveryItem> selectList(String deliveryId) {
        List<SaleDeliveryItem> list = saleDeliveryItemMapper.selectList(deliveryId);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    /**
     * 保存行项目
     */
    public void save(String deliveryId, List<SaleDeliveryItem> itemList) {
        saleDeliveryItemMapper.deleteByDeliveryId(deliveryId);
        if (!CollectionUtils.isEmpty(itemList)) {
            for (int i = 0; i < itemList.size(); i++) {
                SaleDeliveryItem item = itemList.get(i);
                item.setDeliveryId(deliveryId);
            }
            saleDeliveryItemMapper.batchInsert(itemList);
        }
    }

    /**
     * 查询指定销售订单行已交货总量
     */
    public BigDecimal sumDlvQty(String orderNo, String orderItemNo, String excludeDeliveryId) {
        return saleDeliveryItemMapper.sumDlvQtyByOrderNoAndOrderItemNo(orderNo, orderItemNo, excludeDeliveryId);
    }

    /**
     * 删除行项目
     */
    public void deleteByDeliveryId(String deliveryId) {
        saleDeliveryItemMapper.deleteByDeliveryId(deliveryId);
    }
}

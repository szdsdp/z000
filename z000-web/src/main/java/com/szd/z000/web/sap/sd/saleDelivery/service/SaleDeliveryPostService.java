package com.szd.z000.web.sap.sd.saleDelivery.service;

import com.szd.z000.base.orm.domain.sd.saleDelivery.SaleDeliveryPost;
import com.szd.z000.base.orm.mapper.sd.saleDelivery.SaleDeliveryPostMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 销售交货单-过账信息服务
 */
@Service
public class SaleDeliveryPostService {
    @Resource
    private SaleDeliveryPostMapper saleDeliveryPostMapper;

    /**
     * 根据ID查询
     */
    public SaleDeliveryPost selectById(String deliveryId) {
        SaleDeliveryPost saleDeliveryPost = saleDeliveryPostMapper.selectById(deliveryId);
        if (saleDeliveryPost == null) {
            saleDeliveryPost = new SaleDeliveryPost();
        }
        return saleDeliveryPost;
    }

    /**
     * 保存过账信息
     */
    public void save(SaleDeliveryPost post) {
        SaleDeliveryPost exist = saleDeliveryPostMapper.selectById(post.getDeliveryId());
        if (exist == null) {
            saleDeliveryPostMapper.insert(post);
        } else {
            saleDeliveryPostMapper.update(post);
        }
    }
}

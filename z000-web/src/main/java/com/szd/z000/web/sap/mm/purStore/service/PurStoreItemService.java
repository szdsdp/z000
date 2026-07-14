package com.szd.z000.web.sap.mm.purStore.service;

import com.szd.z000.base.orm.domain.mm.purStore.PurStoreItem;
import com.szd.z000.base.orm.mapper.mm.purStore.PurStoreItemMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 物料凭证-行项目服务
 */
@Service
public class PurStoreItemService {
    @Resource
    private PurStoreItemMapper purStoreItemMapper;

    public List<PurStoreItem> selectList(String storeId) {
        List<PurStoreItem> list = purStoreItemMapper.selectList(storeId);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public void save(String storeId, List<PurStoreItem> itemList) {
        purStoreItemMapper.deleteByPurStoreId(storeId);
        if (!CollectionUtils.isEmpty(itemList)) {
            for (int i = 0; i < itemList.size(); i++) {
                PurStoreItem item = itemList.get(i);
                item.setStoreId(storeId);
            }
            purStoreItemMapper.batchInsert(itemList);
        }
    }

    /**
     * 根据前置单据统计关联的入库单数量
     * @param refBussDocId 前置单据业务单号
     * @param refBstp 前置单据业务类型
     * @param bussStatus 单据状态，传'!E'表示非作废状态
     */
    public int countStoreByRefDoc(String refBussDocId, String refBstp, String bussStatus) {
        return purStoreItemMapper.countStoreByRefDoc(refBussDocId, refBstp, bussStatus);
    }

    /**
     * 根据前置单据查询关联的入库单ID列表
     */
    public List<String> selectStoreIdsByRefDoc(String refBussDocId, String refBstp) {
        List<String> list = purStoreItemMapper.selectStoreIdsByRefDoc(refBussDocId, refBstp);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public void deleteByPurStoreId(String storeId) {
        purStoreItemMapper.deleteByPurStoreId(storeId);
    }

    /**
     * 检查订单行项目是否已生成过入库单据
     * @return 已存在的重复(orderNo, orderItemNo)列表
     */
    public List<String> checkDuplicate(List<PurStoreItem> itemList) {
        List<String> duplicates = new ArrayList<>();
        if (!CollectionUtils.isEmpty(itemList)) {
            for (PurStoreItem item : itemList) {
                int count = purStoreItemMapper.countByOrderNoAndOrderItemNo(item.getOrderNo(), item.getOrderItemNo());
                if (count > 0) {
                    duplicates.add("订单:" + item.getOrderNo() + "-" + item.getOrderItemNo());
                }
            }
        }
        return duplicates;
    }
}

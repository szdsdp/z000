package com.szd.z000.web.sap.mm.invCheck.service;

import com.szd.core.client.exception.ClientCustomException;
import com.szd.z000.base.orm.domain.mm.invCheck.InvCheckItem;
import com.szd.z000.base.orm.mapper.mm.invCheck.InvCheckItemMapper;
import com.szd.z000.base.orm.mapper.mm.purStore.PurStoreItemMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 发票校验-行项目服务
 */
@Service
public class InvCheckItemService {
    @Resource
    private InvCheckItemMapper invCheckItemMapper;
    @Resource
    private PurStoreItemMapper purStoreItemMapper;

    public List<InvCheckItem> selectList(String invCheckId) {
        List<InvCheckItem> list = invCheckItemMapper.selectList(invCheckId);
        if (list == null) list = new ArrayList<>();
        return list;
    }

    public void save(String invCheckId, List<InvCheckItem> itemList) {
        invCheckItemMapper.deleteByInvCheckId(invCheckId);
        if (!CollectionUtils.isEmpty(itemList)) {
            for (InvCheckItem item : itemList) item.setInvId(invCheckId);
            invCheckItemMapper.batchInsert(itemList);
        }
    }

    public void deleteByInvCheckId(String invCheckId) {
        invCheckItemMapper.deleteByInvCheckId(invCheckId);
    }

    /**
     * 检查是否可基于物料凭证行创建发票校验（新建场景）
     * @return 已达上限的重复(orderNo, orderItemNo)列表
     */
    public List<String> checkDuplicate(List<InvCheckItem> itemList) {
        return checkDuplicate(itemList, null);
    }

    /**
     * 检查发票校验行项目数量是否超限
     * 其他有效发票校验数量 + 本次提交数量 > 物料凭证数量总和 → 拦截
     * @param itemList 待校验的行项目
     * @param excludeInvId 排除的发票校验单ID（编辑时排除自身，新建时传null）
     * @return 已达上限的重复(orderNo, orderItemNo)列表
     */
    public List<String> checkDuplicate(List<InvCheckItem> itemList, String excludeInvId) {
        List<String> duplicates = new ArrayList<>();
        if (CollectionUtils.isEmpty(itemList)) {
            return duplicates;
        }
        // 按 (orderNo, orderItemNo) 分组汇总本次提交数量
        Map<String, BigDecimal> submittingMap = new LinkedHashMap<>();
        for (InvCheckItem item : itemList) {
            String key = item.getOrderNo() + "&" + item.getOrderItemNo();
            BigDecimal qty = item.getQuantity() != null ? item.getQuantity() : BigDecimal.ZERO;
            submittingMap.merge(key, qty, BigDecimal::add);
        }
        // 逐组校验
        for (Map.Entry<String, BigDecimal> entry : submittingMap.entrySet()) {
            String[] parts = entry.getKey().split("&");
            String orderNo = parts[0];
            String orderItemNo = parts[1];
            BigDecimal submittingQty = entry.getValue();
            // 其他有效发票校验数量（排除当前单据自身）
            BigDecimal existingQty = invCheckItemMapper.countByPoNumberAndPoItem(orderNo, orderItemNo, excludeInvId);
            // 物料凭证数量总和
            BigDecimal purStoreQty = purStoreItemMapper.countValidByOrderNoAndOrderItemNo(orderNo, orderItemNo);
            // 已有 + 本次提交 > 物料凭证总量 → 超限
            if (existingQty.add(submittingQty).compareTo(purStoreQty) > 0) {
                duplicates.add("PO:" + orderNo + "-" + orderItemNo);
            }
        }
        return duplicates;
    }

    /**
     * 校验行项目数量是否超限（排除当前单据自身）
     * @param itemList 待校验的行项目
     * @param excludeInvId 排除的发票校验单ID
     */
    public void checkData(List<InvCheckItem> itemList, String excludeInvId) {
        List<String> duplicates = checkDuplicate(itemList, excludeInvId);
        if (!duplicates.isEmpty()) {
            throw new ClientCustomException("以下订单行的发票校验数量已达物料凭证上限，不可再生成：" + String.join("、", duplicates));
        }
    }
}

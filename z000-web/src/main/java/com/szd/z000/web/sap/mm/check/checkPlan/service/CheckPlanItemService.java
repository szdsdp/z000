package com.szd.z000.web.sap.mm.check.checkPlan.service;

import com.szd.z000.base.orm.domain.mm.check.checkPlan.CheckPlanItem;
import com.szd.z000.base.orm.mapper.mm.check.checkPlan.CheckPlanItemMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 库存盘点-行项目服务
 */
@Service
public class CheckPlanItemService {
    @Resource
    private CheckPlanItemMapper checkPlanItemMapper;

    public List<CheckPlanItem> selectList(String planId) {
        List<CheckPlanItem> list = checkPlanItemMapper.selectList(planId);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public void save(String planId, List<CheckPlanItem> itemList) {
        checkPlanItemMapper.deleteByPlanId(planId);
        if (!CollectionUtils.isEmpty(itemList)) {
            for (int i = 0; i < itemList.size(); i++) {
                CheckPlanItem item = itemList.get(i);
                item.setPlanId(planId);
            }
            checkPlanItemMapper.batchInsert(itemList);
        }
    }

    public void deleteByPlanId(String planId) {
        checkPlanItemMapper.deleteByPlanId(planId);
    }
}

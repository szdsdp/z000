package com.szd.z000.web.sap.mm.purApply.service;

import com.szd.z000.base.orm.domain.mm.purApply.PurApplyItem;
import com.szd.z000.base.orm.mapper.mm.purApply.PurApplyItemMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 采购申请-行项目服务
 */
@Service
public class PurApplyItemService {
    @Resource
    private PurApplyItemMapper purApplyItemMapper;

    public List<PurApplyItem> selectList(String applyId) {
        List<PurApplyItem> list = purApplyItemMapper.selectList(applyId);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public void save(String applyId, List<PurApplyItem> itemList) {
        purApplyItemMapper.deleteByApplyId(applyId);
        if (!CollectionUtils.isEmpty(itemList)) {
            for (int i = 0; i < itemList.size(); i++) {
                PurApplyItem item = itemList.get(i);
                item.setApplyId(applyId);
//                if (item.getItemNo() == null) {
//                    item.setItemNo(String.format("%05d", i + 1));
//                }
            }
            purApplyItemMapper.batchInsert(itemList);
        }
    }

    public void deleteByApplyId(String applyId) {
        purApplyItemMapper.deleteByApplyId(applyId);
    }
}

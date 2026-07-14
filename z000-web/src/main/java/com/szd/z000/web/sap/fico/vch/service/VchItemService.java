package com.szd.z000.web.sap.fico.vch.service;

import com.szd.z000.base.orm.domain.fico.vch.VchItem;
import com.szd.z000.base.orm.mapper.fico.vch.VchItemMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 费用报账-行项目服务
 */
@Service
public class VchItemService {
    @Resource
    private VchItemMapper vchItemMapper;

    public List<VchItem> selectList(String costId) {
        List<VchItem> list = vchItemMapper.selectList(costId);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public void save(String costId, List<VchItem> itemList) {
        vchItemMapper.deleteByCostId(costId);
        if (!CollectionUtils.isEmpty(itemList)) {
            for (int i = 0; i < itemList.size(); i++) {
                VchItem item = itemList.get(i);
                item.setCostId(costId);
                item.setItemNo(i + 1);
            }
            vchItemMapper.batchInsert(itemList);
        }
    }

    public void deleteByCostId(String costId) {
        vchItemMapper.deleteByCostId(costId);
    }
}

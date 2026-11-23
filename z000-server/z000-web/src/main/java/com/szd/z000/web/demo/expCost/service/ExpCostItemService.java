package com.szd.z000.web.demo.expCost.service;

import com.szd.core.client.ClientCore;
import com.szd.z000.base.orm.domain.demo.expCost.ExpCostItem;
import com.szd.z000.base.orm.mapper.demo.expCost.ExpCostItemMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 费用项目Service
 */
@Service
public class ExpCostItemService {
    @Resource
    private ExpCostItemMapper expCostItemMapper;
    @Resource
    private ClientCore clientCore;

    /**
     * 查询费用项目列表
     */
    public List<ExpCostItem> selectList(String bussId) {
        return expCostItemMapper.selectList(bussId);
    }

    /**
     * 新增费用项目
     */
    public void save(String bussId, List<ExpCostItem> itemList) {
        expCostItemMapper.deleteByBussId(bussId);
        if (!CollectionUtils.isEmpty(itemList)) {
            itemList.forEach(item -> {
                item.setBussId(bussId);
            });
            expCostItemMapper.batchInsert(itemList);
        }
    }

    public void deleteById(String bussId) {
        expCostItemMapper.deleteByBussId(bussId);
    }

    /**
     * 费用项目数据校验
     */
    public void checkData(List<ExpCostItem> itemList) {
        if (!CollectionUtils.isEmpty(itemList)) {
            itemList.forEach(t -> {
                clientCore.mdm.checkAE("CORE_BSUB", t.getBsub(), "业务小类", "1");
                clientCore.mdm.checkAE("CORE_ACCS", t.getAccs(), "会计科目", "1");
            });
        }
    }

    /**
     * 获取字段描述
     */
    public void getDesc(List<ExpCostItem> itemList) {
        if (!CollectionUtils.isEmpty(itemList)) {
            for (ExpCostItem item : itemList) {
                item.setBsubName(clientCore.mdm.getDescA("CORE_BSUB", item.getBsub(), "1"));
                item.setAccsName(clientCore.mdm.getDescA("CORE_ACCS", item.getAccs(), "1"));
            }
        }
    }

}

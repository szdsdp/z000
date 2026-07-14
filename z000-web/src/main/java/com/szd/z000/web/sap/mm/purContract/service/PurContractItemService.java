package com.szd.z000.web.sap.mm.purContract.service;

import com.szd.z000.base.orm.domain.mm.purContract.PurContractItem;
import com.szd.z000.base.orm.mapper.mm.purContract.PurContractItemMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 采购合同-行项目服务
 */
@Service
public class PurContractItemService {
    @Resource
    private PurContractItemMapper purContractItemMapper;

    public List<PurContractItem> selectList(String contractId) {
        List<PurContractItem> list = purContractItemMapper.selectList(contractId);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public void save(String contractId, List<PurContractItem> itemList) {
        purContractItemMapper.deleteByContractId(contractId);
        if (!CollectionUtils.isEmpty(itemList)) {
            for (int i = 0; i < itemList.size(); i++) {
                PurContractItem item = itemList.get(i);
                item.setContractId(contractId);
                if (item.getItemNo() == null) {
                    item.setItemNo(String.format("%05d", i + 1));
                }
            }
            purContractItemMapper.batchInsert(itemList);
        }
    }

    public void deleteByContractId(String contractId) {
        purContractItemMapper.deleteByContractId(contractId);
    }
}

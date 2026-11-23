package com.szd.z000.web.sap.sd.saleInv.service;

import com.szd.z000.base.orm.domain.sd.saleInv.SaleInvItem;
import com.szd.z000.base.orm.mapper.sd.saleInv.SaleInvItemMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 销售开票-行项目服务
 */
@Service
public class SaleInvItemService {
    @Resource
    private SaleInvItemMapper saleInvItemMapper;

    /**
     * 根据开票ID查询行项目列表
     */
    public List<SaleInvItem> selectList(String invId) {
        List<SaleInvItem> list = saleInvItemMapper.selectList(invId);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    /**
     * 查询已开票的交货单号
     */
    public List<String> selectInvoicedVbelnVl(List<String> vbelnVlList, String excludeInvId) {
        return saleInvItemMapper.selectInvoicedVbelnVl(vbelnVlList, excludeInvId);
    }

    /**
     * 保存行项目
     */
    public void save(String invId, List<SaleInvItem> itemList) {
        saleInvItemMapper.deleteByInvId(invId);
        if (!CollectionUtils.isEmpty(itemList)) {
            for (int i = 0; i < itemList.size(); i++) {
                SaleInvItem item = itemList.get(i);
                item.setInvId(invId);
            }
            saleInvItemMapper.batchInsert(itemList);
        }
    }
}

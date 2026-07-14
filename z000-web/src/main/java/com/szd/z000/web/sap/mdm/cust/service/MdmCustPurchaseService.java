package com.szd.z000.web.sap.mdm.cust.service;

import com.szd.z000.base.orm.domain.mdm.cust.MdmCustPurchase;
import com.szd.z000.base.orm.mapper.mdm.cust.MdmCustPurchaseMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 客商申请-采购视图
 */
@Service
public class MdmCustPurchaseService {
    @Resource
    private MdmCustPurchaseMapper mdmCustPurchaseMapper;

    public List<MdmCustPurchase> selectList(String applyId) {
        List<MdmCustPurchase> list = mdmCustPurchaseMapper.selectList(applyId);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public void save(String applyId, List<MdmCustPurchase> itemList) {
        mdmCustPurchaseMapper.deleteById(applyId);
        if (!CollectionUtils.isEmpty(itemList)) {
            for (MdmCustPurchase item : itemList) {
                item.setApplyId(applyId);
            }
            mdmCustPurchaseMapper.batchInsert(itemList);
        }
    }

    public void deleteByBussId(String applyId) {
        mdmCustPurchaseMapper.deleteById(applyId);
    }
}

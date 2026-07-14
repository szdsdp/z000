package com.szd.z000.web.sap.mdm.cust.service;

import com.szd.z000.base.orm.domain.mdm.cust.MdmCustSale;
import com.szd.z000.base.orm.domain.mdm.cust.MdmCustSalePartner;
import com.szd.z000.base.orm.mapper.mdm.cust.MdmCustSaleMapper;
import com.szd.z000.base.orm.mapper.mdm.cust.MdmCustSalePartnerMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 客商申请-销售视图
 */
@Service
public class MdmCustSaleService {
    @Resource
    private MdmCustSaleMapper mdmCustSaleMapper;
    @Resource
    private MdmCustSalePartnerMapper mdmCustSalePartnerMapper;

    public List<MdmCustSale> selectList(String applyId) {
        List<MdmCustSale> list = mdmCustSaleMapper.selectList(applyId);
        if (list == null) {
            list = new ArrayList<>();
        } else {
            for (MdmCustSale item : list) {
                item.setPartnerList(mdmCustSalePartnerMapper.selectList(applyId, item.getSaleOrg()));
            }
        }
        return list;
    }

    public void save(String applyId, List<MdmCustSale> itemList) {
        mdmCustSaleMapper.deleteById(applyId);
        if (!CollectionUtils.isEmpty(itemList)) {
            for (MdmCustSale item : itemList) {
                item.setApplyId(applyId);
                if (!CollectionUtils.isEmpty(item.getPartnerList())) {
                    mdmCustSalePartnerMapper.deleteById(applyId, item.getSaleOrg());
                    for (MdmCustSalePartner partner : item.getPartnerList()) {
                        partner.setApplyId(applyId);
                        partner.setItemNo(item.getItemNo());
                        partner.setSaleOrg(item.getSaleOrg());
                    }
                    mdmCustSalePartnerMapper.batchInsert(item.getPartnerList());
                }
            }
            mdmCustSaleMapper.batchInsert(itemList);
        }
    }

    public void deleteByBussId(String applyId) {
        mdmCustSaleMapper.deleteById(applyId);
    }
}

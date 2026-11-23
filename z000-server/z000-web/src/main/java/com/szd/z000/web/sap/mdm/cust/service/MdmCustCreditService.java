package com.szd.z000.web.sap.mdm.cust.service;

import com.szd.z000.base.orm.domain.mdm.cust.MdmCustCredit;
import com.szd.z000.base.orm.mapper.mdm.cust.MdmCustCreditMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 客商申请-信用视图
 */
@Service
public class MdmCustCreditService {
    @Resource
    private MdmCustCreditMapper mdmCustCreditMapper;

    public List<MdmCustCredit> selectList(String applyId) {
        List<MdmCustCredit> list = mdmCustCreditMapper.selectList(applyId);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public void save(String applyId, List<MdmCustCredit> itemList) {
        mdmCustCreditMapper.deleteById(applyId);
        if (!CollectionUtils.isEmpty(itemList)) {
            for (MdmCustCredit item : itemList) {
                item.setApplyId(applyId);
            }
            mdmCustCreditMapper.batchInsert(itemList);
        }
    }

    public void deleteByBussId(String applyId) {
        mdmCustCreditMapper.deleteById(applyId);
    }
}

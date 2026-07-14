package com.szd.z000.web.sap.mdm.cust.service;

import com.szd.z000.base.orm.domain.mdm.cust.MdmCustBkac;
import com.szd.z000.base.orm.mapper.mdm.cust.MdmCustBkacMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 客商申请-银行视图
 */
@Service
public class MdmCustBkacService {
    @Resource
    private MdmCustBkacMapper mdmCustBkacMapper;

    public List<MdmCustBkac> selectList(String applyId) {
        List<MdmCustBkac> list = mdmCustBkacMapper.selectList(applyId);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public void save(String applyId, List<MdmCustBkac> itemList) {
        mdmCustBkacMapper.deleteById(applyId);
        if (!CollectionUtils.isEmpty(itemList)) {
            for (MdmCustBkac item : itemList) {
                item.setApplyId(applyId);
            }
            mdmCustBkacMapper.batchInsert(itemList);
        }
    }

    public void deleteByBussId(String applyId) {
        mdmCustBkacMapper.deleteById(applyId);
    }
}

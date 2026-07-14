package com.szd.z000.web.sap.mdm.cust.service;

import com.szd.z000.base.orm.domain.mdm.cust.MdmCustCmpy;
import com.szd.z000.base.orm.mapper.mdm.cust.MdmCustCmpyMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 客商申请-公司视图
 */
@Service
public class MdmCustCmpyService {
    @Resource
    private MdmCustCmpyMapper mdmCustCmpyMapper;

    public List<MdmCustCmpy> selectList(String applyId) {
        List<MdmCustCmpy> list = mdmCustCmpyMapper.selectList(applyId);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public void save(String applyId, String custFlag, List<MdmCustCmpy> itemList) {
        mdmCustCmpyMapper.deleteByApplyIdAndFlag(applyId, custFlag);
        if (!CollectionUtils.isEmpty(itemList)) {
            for (MdmCustCmpy item : itemList) {
                item.setApplyId(applyId);
                item.setCustFlag(custFlag);
            }
            mdmCustCmpyMapper.batchInsert(itemList);
        }
    }

    public void deleteByBussId(String applyId) {
        mdmCustCmpyMapper.deleteByApplyId(applyId);
    }
}

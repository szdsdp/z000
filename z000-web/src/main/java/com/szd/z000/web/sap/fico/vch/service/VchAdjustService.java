package com.szd.z000.web.sap.fico.vch.service;

import com.github.pagehelper.util.StringUtil;
import com.szd.core.client.ClientCore;
import com.szd.core.client.constants.ClientConstMdm;
import com.szd.core.client.domain.mdm.ClientObjCmpy;
import com.szd.z000.base.orm.domain.fico.vch.VchAdjust;
import com.szd.z000.base.orm.mapper.fico.vch.VchAdjustMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 费用报账-凭证调整分录服务
 */
@Service
public class VchAdjustService {
    @Resource
    private VchAdjustMapper vchAdjustMapper;
    @Resource
    private ClientCore clientCore;

    public List<VchAdjust> selectList(String costId) {
        return vchAdjustMapper.selectList(costId);
    }

    public void save(String costId, List<VchAdjust> list, String operate) {
        List<VchAdjust> oldList = vchAdjustMapper.selectList(costId);
        vchAdjustMapper.deleteByCostId(costId);
        if (!CollectionUtils.isEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                VchAdjust item = list.get(i);
                item.setCostId(costId);
                item.setItemNo(i + 1);
            }
            vchAdjustMapper.batchInsert(list);
        }
        if (!CollectionUtils.isEmpty(oldList)) {
            clientCore.tool.clogSaveB(costId, "itemNo", "z000_fi_vch_adjust", operate, list, oldList);
        }
    }

    public void deleteByCostId(String costId) {
        vchAdjustMapper.deleteByCostId(costId);
    }

    /**
     * 获取字段描述
     */
    public void getDesc(List<VchAdjust> adjustList, String cmpy) {
        if (!CollectionUtils.isEmpty(adjustList)) {
            ClientObjCmpy cmpyObj = clientCore.mdm.getObjA(ClientConstMdm.CORE_CMPY, cmpy, ClientObjCmpy.class);
            for (VchAdjust item : adjustList) {
                if (cmpyObj != null && StringUtil.isNotEmpty(cmpyObj.getAccSet())) {
                    item.setAccsName(clientCore.mdm.getDescB("CORE_ACCS", cmpyObj.getAccSet(), item.getAccs(), "1"));
                }
                item.setSuppName(clientCore.mdm.getDescA("CORE_SUPP", item.getSupp(), "1"));
                item.setCustName(clientCore.mdm.getDescA("CORE_CUST", item.getCust(), "1"));
                item.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", item.getCstc(), "1"));
                item.setPrfcName(clientCore.mdm.getDescA("CORE_PRFC", item.getPrfc(), "1"));
                item.setBuscName(clientCore.mdm.getDescA("CORE_BUSC", item.getBusc(), "1"));
                item.setSegmName(clientCore.mdm.getDescA("CORE_SEGM", item.getSegm(), "1"));
                item.setReasName(clientCore.mdm.getDescA("CORE_REAS", item.getReas(), "1"));
                item.setFuscName(clientCore.mdm.getDescA("CORE_FUSC", item.getFusc(), "1"));
                item.setOrderName(item.getOrderCode() != null ? clientCore.mdm.getDescA("SEXP_ORDE", item.getOrderCode(), "1") : null);
            }
        }
    }
}

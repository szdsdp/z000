package com.szd.z000.web.sap.mm.purOrder.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import com.szd.core.client.util.ClientUtilString;
import com.szd.z000.base.orm.domain.mm.purApply.PurApplyHeader;
import com.szd.z000.base.orm.domain.mm.purOrder.PurOrderHeader;
import com.szd.z000.base.orm.mapper.mm.purApply.PurApplyHeaderMapper;
import com.szd.z000.base.orm.mapper.mm.purOrder.PurOrderHeaderMapper;
import com.szd.z000.service.common.utils.Z000Util;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 采购订单-抬头服务
 */
@Service
public class PurOrderHeaderService {
    @Resource
    private PurOrderHeaderMapper purOrderHeaderMapper;
    @Resource
    private PurApplyHeaderMapper purApplyHeaderMapper;
    @Resource
    private ClientCore clientCore;

    public List<PurOrderHeader> selectList(ClientRpParam param) {
        return purOrderHeaderMapper.selectList(param);
    }

    public PurOrderHeader selectById(String orderId) {
        PurOrderHeader header = purOrderHeaderMapper.selectById(orderId);
        if (header == null) {
            return new PurOrderHeader();
        }
        ClientWfBussBase wfBussBase = clientCore.wf.baseGetObj(orderId);
        if (wfBussBase != null) {
            BeanUtils.copyProperties(wfBussBase, header);
        }
        // 通过采购申请ID反查采购申请业务单号，用于页面展示
        if (ClientUtilString.isNotBlank(header.getApplyId())) {
            PurApplyHeader applyHeader = purApplyHeaderMapper.selectById(header.getApplyId());
            if (applyHeader != null) {
                header.setApplyBussDocId(applyHeader.getBussDocId());
            }
        }
        return header;
    }

    public PurOrderHeader selectObjById(String orderId) {
        PurOrderHeader header = purOrderHeaderMapper.selectById(orderId);
        if (header == null) {
            return new PurOrderHeader();
        }
        return header;
    }

    /**
     * 保存抬头
     */
    public void insert(PurOrderHeader header) {
        purOrderHeaderMapper.insert(header);
    }

    /**
     * 更新抬头
     */
    public void update(PurOrderHeader header) {
        purOrderHeaderMapper.update(header);
    }

    public void updateStatusAndDocId(PurOrderHeader header) {
        purOrderHeaderMapper.updateStatusAndDocId(header.getOrderId(), header.getBussStatus(), header.getBussDocId());
    }

    /**
     * 抬头数据校验
     */
    public void checkData(PurOrderHeader header) {
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_CMPY", header.getCmpy(), "公司代码", "1"));
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_CSTC", header.getCstc(), "部门", "1"));
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_BSTP", header.getBstp(), "业务类型", "1"));
        if (header.getVchType() != null) {
            Z000Util.checkResult(clientCore.mdm.checkA("Z000_MM_CGSQ_SQLX", header.getVchType(), "采购凭证类型", "0"));
        }
        if (header.getPurOrg() != null) {
            Z000Util.checkResult(clientCore.mdm.checkA("Z000_MDM_ORG_PUR", header.getPurOrg(), "采购组织", "0"));
        }
        if (header.getPurGroup() != null) {
            Z000Util.checkResult(clientCore.mdm.checkA("Z000_MM_CGDD_CGZ", header.getPurGroup(), "采购组", "0"));
        }
        if (header.getCurr() != null) {
            Z000Util.checkResult(clientCore.mdm.checkA("CORE_CURR", header.getCurr(), "货币码", "0"));
        }
    }

    /**
     * 根据采购申请ID查询采购订单
     */
    public PurOrderHeader selectByApplyId(String applyId) {
        return purOrderHeaderMapper.selectByApplyId(applyId);
    }

    public void deleteById(String orderId) {
        purOrderHeaderMapper.deleteById(orderId);
    }
}

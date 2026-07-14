package com.szd.z000.web.sap.mm.check.checkPlan.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import com.szd.z000.base.orm.domain.mm.check.checkPlan.CheckPlanHeader;
import com.szd.z000.base.orm.mapper.mm.check.checkPlan.CheckPlanHeaderMapper;
import com.szd.z000.service.common.utils.Z000Util;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 库存盘点-抬头服务
 */
@Service
public class CheckPlanHeaderService {
    @Resource
    private CheckPlanHeaderMapper checkPlanHeaderMapper;
    @Resource
    private ClientCore clientCore;

    public List<CheckPlanHeader> selectList(ClientRpParam param) {
        return checkPlanHeaderMapper.selectList(param);
    }

    public CheckPlanHeader selectById(String planId) {
        CheckPlanHeader header = checkPlanHeaderMapper.selectById(planId);
        if (header == null) {
            return new CheckPlanHeader();
        }
        ClientWfBussBase wfBussBase = clientCore.wf.baseGetObj(planId);
        if (wfBussBase != null) {
            BeanUtils.copyProperties(wfBussBase, header);
        }
        return header;
    }

    public CheckPlanHeader selectObjById(String planId) {
        CheckPlanHeader header = checkPlanHeaderMapper.selectById(planId);
        if (header == null) {
            return new CheckPlanHeader();
        }
        return header;
    }

    public void insert(CheckPlanHeader header) {
        checkPlanHeaderMapper.insert(header);
    }

    public void update(CheckPlanHeader header) {
        checkPlanHeaderMapper.update(header);
    }

    public void updateStatusAndDocId(CheckPlanHeader header) {
        checkPlanHeaderMapper.updateStatusAndDocId(header.getPlanId(), header.getBussStatus(), header.getBussDocId());
    }

    public void checkData(CheckPlanHeader header) {
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_CMPY", header.getCmpy(), "公司代码", "1"));
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_CSTC", header.getCstc(), "部门", "1"));
    }

    public void deleteById(String planId) {
        checkPlanHeaderMapper.deleteById(planId);
    }

    public void updatePlanStatus(String planId, String planStatus) {
        checkPlanHeaderMapper.updatePlanStatus(planId, planStatus);
    }

    public void updateBussStatus(String planId, String bussStatus) {
        checkPlanHeaderMapper.updateBussStatus(planId, bussStatus);
    }
}

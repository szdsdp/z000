package com.szd.z000.web.sap.mm.purApply.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import com.szd.z000.base.orm.domain.mm.purApply.PurApplyHeader;
import com.szd.z000.base.orm.mapper.mm.purApply.PurApplyHeaderMapper;
import com.szd.z000.service.common.utils.Z000Util;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 采购申请-抬头服务
 */
@Service
public class PurApplyHeaderService {
    @Resource
    private PurApplyHeaderMapper purApplyHeaderMapper;
    @Resource
    private ClientCore clientCore;

    public List<PurApplyHeader> selectList(ClientRpParam param) {
        return purApplyHeaderMapper.selectList(param);
    }

    public PurApplyHeader selectById(String applyId) {
        PurApplyHeader header = purApplyHeaderMapper.selectById(applyId);
        if (header == null) {
            return new PurApplyHeader();
        }
        ClientWfBussBase wfBussBase = clientCore.wf.baseGetObj(applyId);
        if (wfBussBase != null) {
            BeanUtils.copyProperties(wfBussBase, header);
        }
        return header;
    }

    public PurApplyHeader selectObjById(String applyId) {
        PurApplyHeader header = purApplyHeaderMapper.selectById(applyId);
        if (header == null) {
            return new PurApplyHeader();
        }
        return header;
    }

    public void insert(PurApplyHeader header) {
        purApplyHeaderMapper.insert(header);
    }

    public void update(PurApplyHeader header) {
        purApplyHeaderMapper.update(header);
    }

    public void updateStatusAndDocId(PurApplyHeader header) {
        purApplyHeaderMapper.updateStatusAndDocId(header.getApplyId(), header.getBussStatus(), header.getBussDocId());
    }

    public void checkData(PurApplyHeader header) {
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_CMPY", header.getCmpy(), "公司代码", "1"));
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_CSTC", header.getCstc(), "部门", "1"));
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_BSTP", header.getBstp(), "业务类型", "1"));
        if (header.getApplyType() != null) {
            Z000Util.checkResult(clientCore.mdm.checkA("Z000_MM_CGSQ_SQLX", header.getApplyType(), "申请类型", "0"));
        }
    }

    public void deleteById(String applyId) {
        purApplyHeaderMapper.deleteById(applyId);
    }
}

package com.szd.z000.web.sap.mm.invCheck.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import com.szd.z000.base.orm.domain.mm.invCheck.InvCheckHeader;
import com.szd.z000.base.orm.mapper.mm.invCheck.InvCheckHeaderMapper;
import com.szd.z000.service.common.utils.Z000Util;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 发票校验-抬头服务
 */
@Service
public class InvCheckHeaderService {
    @Resource
    private InvCheckHeaderMapper invCheckHeaderMapper;
    @Resource
    private ClientCore clientCore;

    public List<InvCheckHeader> selectList(ClientRpParam param) {
        return invCheckHeaderMapper.selectList(param);
    }

    public InvCheckHeader selectById(String invCheckId) {
        InvCheckHeader header = invCheckHeaderMapper.selectById(invCheckId);
        if (header == null) return new InvCheckHeader();
        ClientWfBussBase wfBussBase = clientCore.wf.baseGetObj(invCheckId);
        if (wfBussBase != null) BeanUtils.copyProperties(wfBussBase, header);
        return header;
    }

    public InvCheckHeader selectObjById(String invCheckId) {
        InvCheckHeader header = invCheckHeaderMapper.selectById(invCheckId);
        if (header == null) return new InvCheckHeader();
        return header;
    }

    public void insert(InvCheckHeader header) {
        invCheckHeaderMapper.insert(header);
    }

    public void update(InvCheckHeader header) {
        invCheckHeaderMapper.update(header);
    }

    public void updateStatusAndDocId(InvCheckHeader header) {
        invCheckHeaderMapper.updateStatusAndDocId(header.getInvId(), header.getBussStatus(), header.getBussDocId());
    }

    public void checkData(InvCheckHeader header) {
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_CMPY", header.getCmpy(), "公司代码", "1"));
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_CSTC", header.getCstc(), "部门", "1"));
    }

    public void deleteById(String invCheckId) {
        invCheckHeaderMapper.deleteById(invCheckId);
    }
}

package com.szd.z000.web.sap.fico.vch.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import com.szd.z000.base.orm.domain.fico.vch.VchHeader;
import com.szd.z000.base.orm.mapper.fico.vch.VchHeaderMapper;
import com.szd.z000.service.common.utils.Z000Util;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 费用报账-抬头服务
 */
@Service
public class VchHeaderService {
    @Resource
    private VchHeaderMapper vchHeaderMapper;
    @Resource
    private ClientCore clientCore;

    public List<VchHeader> selectList(ClientRpParam param) {
        return vchHeaderMapper.selectList(param);
    }

    public VchHeader selectById(String costId) {
        VchHeader header = vchHeaderMapper.selectById(costId);
        if (header == null) {
            return new VchHeader();
        }
        ClientWfBussBase wfBussBase = clientCore.wf.baseGetObj(costId);
        if (wfBussBase != null) {
            BeanUtils.copyProperties(wfBussBase, header);
        }
        return header;
    }

    public VchHeader selectObjById(String costId) {
        VchHeader header = vchHeaderMapper.selectById(costId);
        if (header == null) {
            return new VchHeader();
        }
        return header;
    }

    public void insert(VchHeader header) {
        vchHeaderMapper.insert(header);
    }

    public void update(VchHeader header) {
        vchHeaderMapper.update(header);
    }

    public void updateStatusAndDocId(VchHeader header) {
        vchHeaderMapper.updateStatusAndDocId(header.getCostId(), header.getBussStatus(), header.getBussDocId());
    }

    public void checkData(VchHeader header) {
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_CMPY", header.getCmpy(), "公司代码", "1"));
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_CSTC", header.getCstc(), "部门", "1"));
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_BSTP", header.getBstp(), "业务类型", "1"));
    }

    public void deleteById(String costId) {
        vchHeaderMapper.deleteById(costId);
    }
}

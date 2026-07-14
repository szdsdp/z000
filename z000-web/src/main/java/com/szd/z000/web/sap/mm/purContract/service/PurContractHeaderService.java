package com.szd.z000.web.sap.mm.purContract.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import com.szd.z000.base.orm.domain.mm.purContract.PurContractHeader;
import com.szd.z000.base.orm.mapper.mm.purContract.PurContractHeaderMapper;
import com.szd.z000.service.common.utils.Z000Util;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 采购合同-抬头服务
 */
@Service
public class PurContractHeaderService {
    @Resource
    private PurContractHeaderMapper purContractHeaderMapper;
    @Resource
    private ClientCore clientCore;

    public List<PurContractHeader> selectList(ClientRpParam param) {
        return purContractHeaderMapper.selectList(param);
    }

    public PurContractHeader selectById(String contractId) {
        PurContractHeader header = purContractHeaderMapper.selectById(contractId);
        if (header == null) {
            return new PurContractHeader();
        }
        ClientWfBussBase wfBussBase = clientCore.wf.baseGetObj(contractId);
        if (wfBussBase != null) {
            BeanUtils.copyProperties(wfBussBase, header);
        }
        return header;
    }

    public PurContractHeader selectObjById(String contractId) {
        PurContractHeader header = purContractHeaderMapper.selectById(contractId);
        if (header == null) {
            return new PurContractHeader();
        }
        return header;
    }

    public void insert(PurContractHeader header) {
        purContractHeaderMapper.insert(header);
    }

    public void update(PurContractHeader header) {
        purContractHeaderMapper.update(header);
    }

    public void updateStatusAndDocId(PurContractHeader header) {
        purContractHeaderMapper.updateStatusAndDocId(header.getContractId(), header.getBussStatus(), header.getBussDocId());
    }

    public void checkData(PurContractHeader header) {
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_CMPY", header.getCmpy(), "公司代码", "1"));
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_CSTC", header.getCstc(), "部门", "1"));
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_BSTP", header.getBstp(), "业务类型", "1"));
    }

    public void deleteById(String contractId) {
        purContractHeaderMapper.deleteById(contractId);
    }
}

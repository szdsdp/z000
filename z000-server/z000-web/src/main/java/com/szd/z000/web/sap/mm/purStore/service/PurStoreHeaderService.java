package com.szd.z000.web.sap.mm.purStore.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import com.szd.z000.base.orm.domain.mm.purStore.PurStoreHeader;
import com.szd.z000.base.orm.mapper.mm.purStore.PurStoreHeaderMapper;
import com.szd.z000.service.common.utils.Z000Util;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 物料凭证-抬头服务
 */
@Service
public class PurStoreHeaderService {
    @Resource
    private PurStoreHeaderMapper purStoreHeaderMapper;
    @Resource
    private ClientCore clientCore;

    public List<PurStoreHeader> selectList(ClientRpParam param) {
        return purStoreHeaderMapper.selectList(param);
    }

    public PurStoreHeader selectById(String storeId) {
        PurStoreHeader header = purStoreHeaderMapper.selectById(storeId);
        if (header == null) {
            return new PurStoreHeader();
        }
        ClientWfBussBase wfBussBase = clientCore.wf.baseGetObj(storeId);
        if (wfBussBase != null) {
            BeanUtils.copyProperties(wfBussBase, header);
        }
        return header;
    }

    public PurStoreHeader selectObjById(String storeId) {
        PurStoreHeader header = purStoreHeaderMapper.selectById(storeId);
        if (header == null) {
            return new PurStoreHeader();
        }
        return header;
    }

    public void insert(PurStoreHeader header) {
        purStoreHeaderMapper.insert(header);
    }

    public void update(PurStoreHeader header) {
        purStoreHeaderMapper.update(header);
    }

    public void updateStatusAndDocId(PurStoreHeader header) {
        purStoreHeaderMapper.updateStatusAndDocId(header.getStoreId(), header.getBussStatus(), header.getBussDocId());
    }

    public void checkData(PurStoreHeader header) {
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_CMPY", header.getCmpy(), "公司代码", "1"));
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_CSTC", header.getCstc(), "部门", "1"));
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_BSTP", header.getBstp(), "业务类型", "1"));
    }

    public void deleteById(String storeId) {
        purStoreHeaderMapper.deleteById(storeId);
    }
}

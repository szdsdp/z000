package com.szd.z000.web.sap.sd.saleInv.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.z000.base.orm.domain.sd.saleInv.SaleInvHeader;
import com.szd.z000.base.orm.mapper.sd.saleInv.SaleInvHeaderMapper;
import com.szd.z000.service.common.utils.Z000Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 销售开票-抬头服务
 */
@Service
public class SaleInvHeaderService {
    @Resource
    private SaleInvHeaderMapper saleInvHeaderMapper;
    @Resource
    private ClientCore clientCore;

    /**
     * 列表查询
     */
    public List<SaleInvHeader> selectList(ClientRpParam param) {
        return saleInvHeaderMapper.selectList(param);
    }

    /**
     * 根据ID查询
     */
    public SaleInvHeader selectById(String invId) {
        SaleInvHeader header = saleInvHeaderMapper.selectById(invId);
        if (header == null) {
            return new SaleInvHeader();
        }
        ClientWfBussBase wfBussBase = clientCore.wf.baseGetObj(invId);
        if (wfBussBase != null) {
            BeanUtils.copyProperties(wfBussBase, header);
        }
        return header;
    }

    /**
     * 根据ID查询
     */
    public SaleInvHeader selectObjById(String invId) {
        SaleInvHeader header = saleInvHeaderMapper.selectById(invId);
        if (header == null) {
            return new SaleInvHeader();
        }
        return header;
    }

    /**
     * 新增
     */
    public void insert(SaleInvHeader header) {
        saleInvHeaderMapper.insert(header);
    }

    /**
     * 更新
     */
    public void update(SaleInvHeader header) {
        saleInvHeaderMapper.update(header);
    }

    /**
     * 更新业务状态和单据号
     */
    public void updateStatusAndDocId(SaleInvHeader header) {
        saleInvHeaderMapper.updateStatusAndDocId(header.getInvId(), header.getBussStatus(), header.getBussDocId());
    }

    /**
     * 更新SAP返回值
     */
    public void updateSapRet(String invId, String billDoc, String sapMsg, String sapMessId) {
        saleInvHeaderMapper.updateSapRet(invId, billDoc, sapMsg, sapMessId);
    }

    /**
     * 更新冲销SAP返回值
     */
    public void updateReverseSapRet(String invId, String revBillDoc, String revSapMsg, String revSapMessId) {
        saleInvHeaderMapper.updateReverseSapRet(invId, revBillDoc, revSapMsg, revSapMessId);
    }

    /**
     * 业务数据校验
     */
    public void checkData(SaleInvHeader header) {
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_CMPY", header.getCmpy(), "公司代码", "1"));
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_CSTC", header.getCstc(), "部门", "1"));
        if (StringUtils.isBlank(header.getBudat())) {
            throw new ClientCustomException("过账日期不能为空");
        }
        if (StringUtils.isBlank(header.getDocType())) {
            throw new ClientCustomException("发票类型不能为空");
        }
    }

    /**
     * 删除
     */
    public void deleteById(String invId) {
        saleInvHeaderMapper.deleteById(invId);
    }
}

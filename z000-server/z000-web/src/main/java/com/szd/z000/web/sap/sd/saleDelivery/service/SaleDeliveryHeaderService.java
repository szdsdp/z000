package com.szd.z000.web.sap.sd.saleDelivery.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import com.szd.z000.base.orm.domain.sd.saleDelivery.SaleDeliveryHeader;
import com.szd.z000.base.orm.mapper.sd.saleDelivery.SaleDeliveryHeaderMapper;
import com.szd.z000.service.common.utils.Z000Util;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 销售交货单-抬头服务
 */
@Service
public class SaleDeliveryHeaderService {
    @Resource
    private SaleDeliveryHeaderMapper saleDeliveryHeaderMapper;
    @Resource
    private ClientCore clientCore;

    /**
     * 列表查询
     */
    public List<SaleDeliveryHeader> selectList(ClientRpParam param) {
        return saleDeliveryHeaderMapper.selectList(param);
    }

    /**
     * 根据ID查询
     */
    public SaleDeliveryHeader selectById(String deliveryId) {
        SaleDeliveryHeader header = saleDeliveryHeaderMapper.selectById(deliveryId);
        if (header == null) {
            return new SaleDeliveryHeader();
        }
        ClientWfBussBase wfBussBase = clientCore.wf.baseGetObj(deliveryId);
        if (wfBussBase != null) {
            BeanUtils.copyProperties(wfBussBase, header);
        }
        return header;
    }

    /**
     * 根据ID查询
     */
    public SaleDeliveryHeader selectObjById(String deliveryId) {
        SaleDeliveryHeader header = saleDeliveryHeaderMapper.selectById(deliveryId);
        if (header == null) {
            return new SaleDeliveryHeader();
        }
        return header;
    }

    /**
     * 根据SAP交货单号查询
     */
    public SaleDeliveryHeader selectByVbeln(String vbeln) {
        return saleDeliveryHeaderMapper.selectByVbeln(vbeln);
    }

    /**
     * 新增
     */
    public void insert(SaleDeliveryHeader header) {
        saleDeliveryHeaderMapper.insert(header);
    }

    /**
     * 更新
     */
    public void update(SaleDeliveryHeader header) {
        saleDeliveryHeaderMapper.update(header);
    }

    /**
     * 更新业务状态和单据号
     */
    public void updateStatusAndDocId(SaleDeliveryHeader header) {
        saleDeliveryHeaderMapper.updateStatusAndDocId(header.getDeliveryId(), header.getBussStatus(), header.getBussDocId());
    }

    /**
     * 业务数据校验
     */
    public void checkData(SaleDeliveryHeader header) {
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_CMPY", header.getCmpy(), "公司代码", "1"));
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_CSTC", header.getCstc(), "部门", "1"));
    }

    /**
     * 删除
     */
    public void deleteById(String deliveryId) {
        saleDeliveryHeaderMapper.deleteById(deliveryId);
    }
}

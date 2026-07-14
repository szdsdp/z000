package com.szd.z000.web.sap.sd.saleOrder.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import com.szd.z000.base.orm.domain.sd.saleOrder.SaleOrderHeader;
import com.szd.z000.base.orm.mapper.sd.saleOrder.SaleOrderHeaderMapper;
import com.szd.z000.service.common.utils.Z000Util;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 销售订单创建-抬头服务
 */
@Service
public class SaleOrderHeaderService {
    @Resource
    private SaleOrderHeaderMapper saleOrderHeaderMapper;
    @Resource
    private ClientCore clientCore;

    /**
     * 列表查询
     */
    public List<SaleOrderHeader> selectList(ClientRpParam param) {
        return saleOrderHeaderMapper.selectList(param);
    }

    /**
     * 根据ID查询
     */
    public SaleOrderHeader selectById(String orderId) {
        SaleOrderHeader header = saleOrderHeaderMapper.selectById(orderId);
        if (header == null) {
            return new SaleOrderHeader();
        }
        ClientWfBussBase wfBussBase = clientCore.wf.baseGetObj(orderId);
        if (wfBussBase != null) {
            BeanUtils.copyProperties(wfBussBase, header);
        }
        return header;
    }

    /**
     * 根据ID查询
     */
    public SaleOrderHeader selectObjById(String orderId) {
        SaleOrderHeader header = saleOrderHeaderMapper.selectById(orderId);
        if (header == null) {
            return new SaleOrderHeader();
        }
        return header;
    }

    /**
     * 新增
     */
    public void insert(SaleOrderHeader header) {
        saleOrderHeaderMapper.insert(header);
    }

    /**
     * 更新
     */
    public void update(SaleOrderHeader header) {
        saleOrderHeaderMapper.update(header);
    }

    /**
     * 更新业务状态和单据号
     */
    public void updateStatusAndDocId(SaleOrderHeader header) {
        saleOrderHeaderMapper.updateStatusAndDocId(header.getOrderId(), header.getBussStatus(), header.getBussDocId());
    }

    /**
     * 业务数据校验
     */
    public void checkData(SaleOrderHeader header) {
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_CMPY", header.getCmpy(), "公司代码", "1"));
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_CSTC", header.getCstc(), "部门", "1"));
    }

    /**
     * 删除
     */
    public void deleteById(String orderId) {
        saleOrderHeaderMapper.deleteById(orderId);
    }
}

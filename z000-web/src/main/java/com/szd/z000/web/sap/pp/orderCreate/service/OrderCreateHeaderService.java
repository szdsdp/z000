package com.szd.z000.web.sap.pp.orderCreate.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import com.szd.z000.base.orm.domain.pp.orderCreate.OrderCreateHeader;
import com.szd.z000.base.orm.mapper.pp.orderCreate.OrderCreateHeaderMapper;
import com.szd.z000.service.common.utils.Z000Util;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 生产订单创建-抬头服务
 */
@Service
public class OrderCreateHeaderService {
    @Resource
    private OrderCreateHeaderMapper orderCreateHeaderMapper;
    @Resource
    private ClientCore clientCore;

    public List<OrderCreateHeader> selectList(ClientRpParam param) {
        return orderCreateHeaderMapper.selectList(param);
    }

    public OrderCreateHeader selectById(String applyId) {
        OrderCreateHeader header = orderCreateHeaderMapper.selectById(applyId);
        if (header == null) {
            return new OrderCreateHeader();
        }
        ClientWfBussBase wfBussBase = clientCore.wf.baseGetObj(applyId);
        if (wfBussBase != null) {
            BeanUtils.copyProperties(wfBussBase, header);
        }
        return header;
    }

    public OrderCreateHeader selectObjById(String applyId) {
        OrderCreateHeader header = orderCreateHeaderMapper.selectById(applyId);
        if (header == null) {
            return new OrderCreateHeader();
        }
        return header;
    }

    public void insert(OrderCreateHeader header) {
        orderCreateHeaderMapper.insert(header);
    }

    public void update(OrderCreateHeader header) {
        orderCreateHeaderMapper.update(header);
    }

    public void updateStatusAndDocId(OrderCreateHeader header) {
        orderCreateHeaderMapper.updateStatusAndDocId(header.getApplyId(), header.getBussStatus(), header.getBussDocId());
    }

    public void checkData(OrderCreateHeader header) {
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_CMPY", header.getCmpy(), "公司代码", "1"));
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_CSTC", header.getCstc(), "部门", "1"));
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_BSTP", header.getBstp(), "业务类型", "1"));
    }

    public void deleteById(String applyId) {
        orderCreateHeaderMapper.deleteById(applyId);
    }
}

package com.szd.z000.web.sap.pp.orderReport.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import com.szd.z000.base.orm.domain.pp.orderReport.OrderReportHeader;
import com.szd.z000.base.orm.mapper.pp.orderReport.OrderReportHeaderMapper;
import com.szd.z000.service.common.utils.Z000Util;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 生产订单报工-抬头服务
 */
@Service
public class OrderReportHeaderService {
    @Resource
    private OrderReportHeaderMapper orderReportHeaderMapper;
    @Resource
    private ClientCore clientCore;

    public List<OrderReportHeader> selectList(ClientRpParam param) {
        return orderReportHeaderMapper.selectList(param);
    }

    public OrderReportHeader selectById(String reportId) {
        OrderReportHeader header = orderReportHeaderMapper.selectById(reportId);
        if (header == null) {
            return new OrderReportHeader();
        }
        ClientWfBussBase wfBussBase = clientCore.wf.baseGetObj(reportId);
        if (wfBussBase != null) {
            BeanUtils.copyProperties(wfBussBase, header);
        }
        return header;
    }

    public OrderReportHeader selectObjById(String reportId) {
        OrderReportHeader header = orderReportHeaderMapper.selectById(reportId);
        if (header == null) {
            return new OrderReportHeader();
        }
        return header;
    }

    public void insert(OrderReportHeader header) {
        orderReportHeaderMapper.insert(header);
    }

    public void update(OrderReportHeader header) {
        orderReportHeaderMapper.update(header);
    }

    public void updateStatusAndDocId(OrderReportHeader header) {
        orderReportHeaderMapper.updateStatusAndDocId(header.getReportId(), header.getBussStatus(), header.getBussDocId());
    }

    public void checkData(OrderReportHeader header) {
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_CMPY", header.getCmpy(), "公司代码", "1"));
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_CSTC", header.getCstc(), "部门", "1"));
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_BSTP", header.getBstp(), "业务类型", "1"));
    }

    public void deleteById(String reportId) {
        orderReportHeaderMapper.deleteById(reportId);
    }
}

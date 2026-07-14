package com.szd.z000.web.sap.pp.orderReport.domain;

import com.szd.core.client.domain.wf.ClientWfParamEvt;
import lombok.Data;

/**
 * 生产订单报工-参数VO
 */
@Data
public class OrderReportParamVO {
    private OrderReportDataVO data;
    private ClientWfParamEvt wfEvt;
}

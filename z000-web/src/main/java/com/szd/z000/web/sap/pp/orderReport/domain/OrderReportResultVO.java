package com.szd.z000.web.sap.pp.orderReport.domain;

import com.szd.core.client.domain.wf.ClientWfOper;
import lombok.Data;

/**
 * 生产订单报工-返回VO
 */
@Data
public class OrderReportResultVO {
    private OrderReportDataVO data;
    private ClientWfOper wfOper;
}

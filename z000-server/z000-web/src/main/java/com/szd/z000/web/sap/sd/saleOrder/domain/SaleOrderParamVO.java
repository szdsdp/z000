package com.szd.z000.web.sap.sd.saleOrder.domain;

import com.szd.core.client.domain.wf.ClientWfParamEvt;
import lombok.Data;

/**
 * 销售订单创建-参数VO
 */
@Data
public class SaleOrderParamVO {
    private SaleOrderDataVO data;
    private ClientWfParamEvt wfEvt;
}

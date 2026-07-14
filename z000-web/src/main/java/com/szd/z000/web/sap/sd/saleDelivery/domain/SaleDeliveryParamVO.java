package com.szd.z000.web.sap.sd.saleDelivery.domain;

import com.szd.core.client.domain.wf.ClientWfParamEvt;
import lombok.Data;

/**
 * 销售交货单-参数VO
 */
@Data
public class SaleDeliveryParamVO {
    private SaleDeliveryDataVO data;
    private ClientWfParamEvt wfEvt;
}

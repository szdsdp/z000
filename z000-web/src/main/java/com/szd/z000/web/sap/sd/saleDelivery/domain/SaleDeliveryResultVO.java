package com.szd.z000.web.sap.sd.saleDelivery.domain;

import com.szd.core.client.domain.wf.ClientWfOper;
import lombok.Data;

/**
 * 销售交货单-结果VO
 */
@Data
public class SaleDeliveryResultVO {
    private SaleDeliveryDataVO data;
    private ClientWfOper wfOper;
}

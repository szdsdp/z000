package com.szd.z000.web.sap.sd.saleOrder.domain;

import com.szd.core.client.domain.wf.ClientWfOper;
import lombok.Data;

/**
 * 销售订单创建-结果VO
 */
@Data
public class SaleOrderResultVO {
    private SaleOrderDataVO data;
    private ClientWfOper wfOper;
}

package com.szd.z000.web.sap.sd.saleInv.domain;

import com.szd.core.client.domain.wf.ClientWfParamEvt;
import lombok.Data;

/**
 * 销售开票-参数VO
 */
@Data
public class SaleInvParamVO {
    private SaleInvDataVO data;
    private ClientWfParamEvt wfEvt;
}

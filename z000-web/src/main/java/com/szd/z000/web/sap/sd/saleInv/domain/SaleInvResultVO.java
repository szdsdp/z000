package com.szd.z000.web.sap.sd.saleInv.domain;

import com.szd.core.client.domain.wf.ClientWfOper;
import lombok.Data;

/**
 * 销售开票-结果VO
 */
@Data
public class SaleInvResultVO {
    private SaleInvDataVO data;
    private ClientWfOper wfOper;
}

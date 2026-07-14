package com.szd.z000.web.sap.mm.check.checkPlan.domain;

import com.szd.core.client.domain.wf.ClientWfParamEvt;
import lombok.Data;

/**
 * 库存盘点-参数VO
 */
@Data
public class CheckPlanParamVO {
    private CheckPlanDataVO data;
    private ClientWfParamEvt wfEvt;
}

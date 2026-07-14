package com.szd.z000.web.sap.mm.check.checkPlan.domain;

import com.szd.core.client.domain.wf.ClientWfOper;
import lombok.Data;

/**
 * 库存盘点-结果VO
 */
@Data
public class CheckPlanResultVO {
    private CheckPlanDataVO data;
    private ClientWfOper wfOper;
}

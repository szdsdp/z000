package com.szd.z000.web.demo.expCost.domain;
import com.szd.core.client.domain.wf.ClientWfParamEvt;

import lombok.Data;

/**
 * 业务参数
 */
@Data
public class ExpCostParamVO {
    /** 业务表单-公共数据 */
    private ExpCostDataVO data;
    /** 流程参数 */
    private ClientWfParamEvt wfEvt;
}

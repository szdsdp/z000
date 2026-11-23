package com.szd.z000.web.demo.expCost.domain;

import com.szd.core.client.domain.wf.ClientWfOper;
import lombok.Data;

/**
 * 业务参数
 */
@Data
public class ExpCostResultVO {
    /** 业务表单数据 */
    private ExpCostDataVO data;
    /** 流程操作授权 */
    private ClientWfOper wfOper;
}


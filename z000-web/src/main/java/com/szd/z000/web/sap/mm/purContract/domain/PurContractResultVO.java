package com.szd.z000.web.sap.mm.purContract.domain;

import com.szd.core.client.domain.wf.ClientWfOper;
import lombok.Data;

/**
 * 采购合同-返回VO
 */
@Data
public class PurContractResultVO {
    /**
     * 业务表单数据
     */
    private PurContractDataVO data;
    /**
     * 流程操作授权
     */
    private ClientWfOper wfOper;
}

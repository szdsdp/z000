package com.szd.z000.web.sap.mm.purContract.domain;

import com.szd.core.client.domain.wf.ClientWfParamEvt;
import lombok.Data;

/**
 * 采购合同-参数VO
 */
@Data
public class PurContractParamVO {
    /**
     * 业务表单数据
     */
    private PurContractDataVO data;
    /**
     * 流程参数
     */
    private ClientWfParamEvt wfEvt;
}

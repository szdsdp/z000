package com.szd.z000.web.sap.mm.purApply.domain;

import com.szd.core.client.domain.wf.ClientWfParamEvt;
import lombok.Data;

/**
 * 采购申请-参数VO
 */
@Data
public class PurApplyParamVO {
    /**
     * 业务表单数据
     */
    private PurApplyDataVO data;
    /**
     * 流程参数
     */
    private ClientWfParamEvt wfEvt;
}

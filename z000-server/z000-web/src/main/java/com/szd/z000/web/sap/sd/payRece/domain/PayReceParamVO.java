package com.szd.z000.web.sap.sd.payRece.domain;

import com.szd.core.client.domain.wf.ClientWfParamEvt;
import lombok.Data;

/**
 * 回款认领业务参数
 */
@Data
public class PayReceParamVO {
    /**
     * 业务表单数据
     */
    private PayReceDataVO data;
    /**
     * 流程参数
     */
    private ClientWfParamEvt wfEvt;
}

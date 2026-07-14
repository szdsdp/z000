package com.szd.z000.web.sap.mm.purOrder.domain;

import com.szd.core.client.domain.wf.ClientWfParamEvt;
import lombok.Data;

/**
 * 采购订单-参数VO
 */
@Data
public class PurOrderParamVO {
    /**
     * 业务表单数据
     */
    private PurOrderDataVO data;
    /**
     * 流程参数
     */
    private ClientWfParamEvt wfEvt;
}

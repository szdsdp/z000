package com.szd.z000.web.sap.pp.orderCreate.domain;

import com.szd.core.client.domain.wf.ClientWfParamEvt;
import lombok.Data;

/**
 * 生产订单创建-参数VO
 */
@Data
public class OrderCreateParamVO {
    /**
     * 业务表单数据
     */
    private OrderCreateDataVO data;
    /**
     * 流程参数
     */
    private ClientWfParamEvt wfEvt;
}

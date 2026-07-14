package com.szd.z000.web.sap.pp.orderCreate.domain;

import com.szd.core.client.domain.wf.ClientWfOper;
import lombok.Data;

/**
 * 生产订单创建-返回VO
 */
@Data
public class OrderCreateResultVO {
    /**
     * 业务表单数据
     */
    private OrderCreateDataVO data;
    /**
     * 流程操作授权
     */
    private ClientWfOper wfOper;
}

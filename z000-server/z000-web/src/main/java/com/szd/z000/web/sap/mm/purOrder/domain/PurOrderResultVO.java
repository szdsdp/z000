package com.szd.z000.web.sap.mm.purOrder.domain;

import com.szd.core.client.domain.wf.ClientWfOper;
import lombok.Data;

/**
 * 采购订单-返回VO
 */
@Data
public class PurOrderResultVO {
    /**
     * 业务表单数据
     */
    private PurOrderDataVO data;
    /**
     * 流程操作授权
     */
    private ClientWfOper wfOper;
}

package com.szd.z000.web.sap.sd.payRece.domain;

import com.szd.core.client.domain.wf.ClientWfOper;
import lombok.Data;

/**
 * 回款认领返回对象
 */
@Data
public class PayReceResultVO {
    /**
     * 业务表单数据
     */
    private PayReceDataVO data;
    /**
     * 流程操作授权
     */
    private ClientWfOper wfOper;
}

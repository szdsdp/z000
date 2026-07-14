package com.szd.z000.web.sap.mm.purStore.domain;

import com.szd.core.client.domain.wf.ClientWfOper;
import lombok.Data;

/**
 * 物料凭证-返回VO
 */
@Data
public class PurStoreResultVO {
    /**
     * 业务表单数据
     */
    private PurStoreDataVO data;
    /**
     * 流程操作授权
     */
    private ClientWfOper wfOper;
}

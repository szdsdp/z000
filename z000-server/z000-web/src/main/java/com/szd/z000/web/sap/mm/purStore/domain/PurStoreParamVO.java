package com.szd.z000.web.sap.mm.purStore.domain;

import com.szd.core.client.domain.wf.ClientWfParamEvt;
import lombok.Data;

/**
 * 物料凭证-参数VO
 */
@Data
public class PurStoreParamVO {
    /**
     * 业务表单数据
     */
    private PurStoreDataVO data;
    /**
     * 流程参数
     */
    private ClientWfParamEvt wfEvt;
}

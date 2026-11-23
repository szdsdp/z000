package com.szd.z000.web.sap.mm.purApply.domain;

import com.szd.core.client.domain.wf.ClientWfOper;
import lombok.Data;

/**
 * 采购申请-返回VO
 */
@Data
public class PurApplyResultVO {
    /**
     * 业务表单数据
     */
    private PurApplyDataVO data;
    /**
     * 流程操作授权
     */
    private ClientWfOper wfOper;
}

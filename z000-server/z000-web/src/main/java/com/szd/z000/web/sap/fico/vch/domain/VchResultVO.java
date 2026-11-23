package com.szd.z000.web.sap.fico.vch.domain;

import com.szd.core.client.domain.wf.ClientWfOper;
import lombok.Data;

/**
 * 费用报账-返回VO
 */
@Data
public class VchResultVO {
    /**
     * 业务表单数据
     */
    private VchDataVO data;
    /**
     * 流程操作授权
     */
    private ClientWfOper wfOper;
}

package com.szd.z000.web.sap.fico.vch.domain;

import com.szd.core.client.domain.wf.ClientWfParamEvt;
import lombok.Data;

/**
 * 费用报账-参数VO
 */
@Data
public class VchParamVO {
    /**
     * 业务表单数据
     */
    private VchDataVO data;
    /**
     * 流程参数
     */
    private ClientWfParamEvt wfEvt;
}

package com.szd.z000.web.sap.mm.invCheck.domain;

import com.szd.core.client.domain.wf.ClientWfParamEvt;
import lombok.Data;

/**
 * 发票校验-参数VO
 */
@Data
public class InvCheckParamVO {
    private InvCheckDataVO data;
    private ClientWfParamEvt wfEvt;
}

package com.szd.z000.web.sap.mm.invCheck.domain;

import com.szd.core.client.domain.wf.ClientWfOper;
import lombok.Data;

/**
 * 发票校验-返回VO
 */
@Data
public class InvCheckResultVO {
    private InvCheckDataVO data;
    private ClientWfOper wfOper;
}

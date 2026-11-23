package com.szd.z000.web.sap.mm.check.checkTask.domain;

import com.szd.core.client.domain.wf.ClientWfParamEvt;
import lombok.Data;

/**
 * 盘点任务-参数VO
 */
@Data
public class CheckTaskParamVO {
    private CheckTaskDataVO data;
    private ClientWfParamEvt wfEvt;
}

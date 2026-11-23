package com.szd.z000.web.sap.mdm.mtlcj.domain;

import com.szd.core.client.domain.wf.ClientWfParamEvt;
import lombok.Data;

/**
 * 物料主数据创建申请-参数VO
 */
@Data
public class MtlcjParamVO {
    /** 业务表单数据 */
    private MtlcjDataVO data;
    /** 流程参数 */
    private ClientWfParamEvt wfEvt;
}

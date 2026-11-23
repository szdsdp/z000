package com.szd.z000.web.sap.mdm.mtlcj.domain;

import com.szd.core.client.domain.wf.ClientWfOper;
import lombok.Data;

/**
 * 物料主数据创建申请-返回VO
 */
@Data
public class MtlcjResultVO {
    /** 业务表单数据 */
    private MtlcjDataVO data;
    /** 流程操作授权 */
    private ClientWfOper wfOper;
}

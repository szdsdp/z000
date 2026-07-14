package com.szd.z000.web.sap.mdm.cust.domain;

import com.szd.core.client.domain.wf.ClientWfOper;
import lombok.Data;

@Data
public class MdmCustResultVO {
    /**
     * 业务表单数据
     */
    private MdmCustDataVO data;
    /**
     * 流程操作授权
     */
    private ClientWfOper wfOper;
}

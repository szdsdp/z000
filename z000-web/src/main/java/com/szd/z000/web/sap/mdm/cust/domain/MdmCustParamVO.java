package com.szd.z000.web.sap.mdm.cust.domain;

import com.szd.core.client.domain.wf.ClientWfParamEvt;
import lombok.Data;

@Data
public class MdmCustParamVO {

    /**
     * 业务表单数据
     */
    private MdmCustDataVO data;
    /**
     * 流程参数
     */
    private ClientWfParamEvt wfEvt;
}

package com.szd.z000.web.sap.sd.payFlowBill.domain;

import lombok.Data;

import java.util.List;

/**
 * 更新客户参数
 */
@Data
public class UpdateCustParam {
    /**
     * 客户编码
     */
    private String cust;
    /**
     * 系统票号列表
     */
    private List<String> sysId;
    /**
     * 大区
     */
    private String region;
}

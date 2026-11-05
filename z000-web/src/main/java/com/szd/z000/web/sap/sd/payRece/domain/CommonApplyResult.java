package com.szd.z000.web.sap.sd.payRece.domain;

import lombok.Data;

/**
 * 回款认领创建返回
 */
@Data
public class CommonApplyResult {
    /**
     * 业务id
     */
    private String bussId;
    /**
     * 路由编码
     */
    private String routerCode;
}

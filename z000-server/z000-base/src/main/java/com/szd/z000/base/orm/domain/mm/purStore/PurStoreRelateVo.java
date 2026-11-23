package com.szd.z000.base.orm.domain.mm.purStore;

import lombok.Data;

/***
 * 关联单据vo类
 */
@Data
public class PurStoreRelateVo {
    /**
     * bussId
     */
    private String bussId;

    /**
     * 单据号
     */
    private String bussDocId;

    /**
     * 路由
     */
    private String routerCode;

    /**
     * 单据状态
     */
    private String bussStatus;
    private String bussStatusName;

    /**
     * 单据类型
     */
    private String bussDocType;
    /**
     * 业务类型
     */
    private String bstp;
}

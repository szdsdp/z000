package com.szd.z000.base.orm.domain.mm.check.checkPlan;

import lombok.Data;

/***
 * 关联单据vo类
 */
@Data
public class CheckPlanRelateVo {
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

    /***
     * 任务状态
     */
    private String taskStatus;
    private String taskStatusName;
}


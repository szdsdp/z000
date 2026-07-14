package com.szd.z000.base.orm.domain.mm.check.checkTask;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import lombok.Data;

import java.util.Date;

/**
 * 盘点任务-抬头
 */
@Data
public class CheckTaskHeader extends ClientWfBussBase {
    /**
     * 任务ID
     */
    private String taskId;
    /**
     * 关联盘点计划ID
     */
    private String planId;
    private String checkPlanBussDocId;
    /**
     * 工厂
     */
    private String fact;
    private String factName;
    /**
     * 库位
     */
    private String store;
    private String storeName;
    /**
     * 盘点状态(01=盘点中/02=已完成)
     */
    private String taskStatus;
    private String taskStatusName;
    /**
     * 是否盲盘(从计划继承，1=是/0=否)
     */
    private String blindFlag;

    /**
     * 业务状态
     */
    private String bussStatus;
    private String bussStatusName;

    /**
     * 业务日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date bussDate;
}

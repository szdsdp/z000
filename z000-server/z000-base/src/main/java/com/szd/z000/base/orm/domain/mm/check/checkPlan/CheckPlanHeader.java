package com.szd.z000.base.orm.domain.mm.check.checkPlan;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import lombok.Data;

import java.util.Date;

/**
 * 库存盘点-抬头
 */
@Data
public class CheckPlanHeader extends ClientWfBussBase {
    /**
     * 盘点单ID
     */
    private String planId;
    /**
     * 凭证过帐日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date postDate;
    /**
     * 凭证日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date vchDate;
    /**
     * 物料凭证号
     */
    private String mblnr;
    /**
     * 物料凭证年度
     */
    private String mjahr;
    /**
     * SAP消息
     */
    private String sapMsg;
    /**
     * SAP消息ID
     */
    private String sapMessId;
    /**
     * 业务日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date bussDate;
    /**
     * 业务状态
     */
    private String bussStatus;
    private String bussStatusName;
    /**
     * 冲销日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date budat;
    /**
     * 冲销物料凭证
     */
    private String revMblnr;
    /**
     * 冲销物料凭证年度
     */
    private String revMjahr;
    /**
     * 冲销SAP消息
     */
    private String revSapMsg;
    /**
     * 冲销SAP消息ID
     */
    private String revSapMessId;
    /**
     * 盘点月度(yyyyMM)
     */
    private String month;
    /**
     * 是否盲盘(1=是/0=否)
     */
    private String blindFlag;
    /**
     * 计划进度(01=未下发/02=盘点中/03=盘点完成)
     */
    private String planStatus;
}

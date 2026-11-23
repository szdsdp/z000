package com.szd.z000.base.orm.domain.demo.expCost;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import lombok.Data;

import java.util.Date;

/**
 * 抬头表
 */
@Data
public class ExpCostHeader extends ClientWfBussBase {
    /** 业务ID */
    private String bussId;
    /** 制单人公司代码 */
    private String cmpyCreate;
    private String cmpyCreateName;
    /** 制单人成本中心 */
    private String cstcCreate;
    private String cstcCreateName;
    /** 报账人 */
    private String userId;
    private String userName;
    /** 业务日期 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date bussDate;
    /** 币种 */
    private String curr;
    private String currName;
    /** 状态 */
    private String bussStatus;
    private String bussStatusName;
}

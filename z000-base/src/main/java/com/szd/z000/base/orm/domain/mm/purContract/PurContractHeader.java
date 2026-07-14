package com.szd.z000.base.orm.domain.mm.purContract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import lombok.Data;

import java.util.Date;

/**
 * 采购合同-抬头
 */
@Data
public class PurContractHeader extends ClientWfBussBase {
    /**
     * 采购合同ID
     */
    private String contractId;
    /**
     * 类型
     */
    private String type;
    private String typeName;
    /**
     * 供应商
     */
    private String supp;
    private String suppName;
    /**
     * 采购组织
     */
    private String purOrg;
    private String purOrgName;
    /**
     * 采购组
     */
    private String purGroup;
    private String purGroupName;
    /**
     * 货币
     */
    private String curr;
    private String currName;
    /**
     * 有效开始日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startDate;
    /**
     * 有效结束日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 采购合同号
     */
    private String contractNo;
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
     * 附件
     */
    private String fileObjId;
}

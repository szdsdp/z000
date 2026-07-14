package com.szd.z000.base.orm.domain.mm.purOrder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import lombok.Data;

import java.util.Date;

/**
 * 采购订单-抬头
 */
@Data
public class PurOrderHeader extends ClientWfBussBase {
    /**
     * 采购订单ID
     */
    private String orderId;
    /**
     * 采购凭证号
     */
    private String vchNo;
    /**
     * 采购凭证类型
     */
    private String vchType;
    private String vchTypeName;
    /**
     * 供应商账号
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
     * 货币码
     */
    private String curr;
    private String currName;
    /**
     * 采购凭证日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date vchDate;
    /**
     * 文本行
     */
    private String textLine;

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
     * 采购申请ID
     */
    private String applyId;
    /**
     * 采购申请业务单号（展示用，不存库）
     */
    private String applyBussDocId;
    /**
     * 入库单数量（展示用，不存库）
     */
    private Integer storeCount;
    /**
     * SAP已入库数量（展示用，不存库）
     */
    private Integer sapStoreCount;
    /**
     * SAP消息
     */
    private String sapMsg;
    /**
     * SAP消息ID
     */
    private String sapMessId;
}

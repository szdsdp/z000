package com.szd.z000.base.orm.domain.mm.purApply;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import lombok.Data;

import java.util.Date;

/**
 * 采购申请-抬头
 */
@Data
public class PurApplyHeader extends ClientWfBussBase {
    /**
     * 申请ID
     */
    private String applyId;
    /**
     * 申请类型
     */
    private String applyType;
    private String applyTypeName;
    /**
     * 采购申请号
     */
    private String applyNo;
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

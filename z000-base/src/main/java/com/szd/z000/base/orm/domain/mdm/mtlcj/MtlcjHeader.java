package com.szd.z000.base.orm.domain.mdm.mtlcj;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import lombok.Data;

import java.util.Date;

/**
 * 物料主数据创建申请-基本信息
 */
@Data
public class MtlcjHeader extends ClientWfBussBase {
    /**
     * 申请ID
     */
    private String applyId;
    /**
     * 物料编码(SAP返回)
     */
    private String mtlCode;
    /**
     * 物料名称(短文本)
     */
    private String mtlName;
    /**
     * 行业领域
     */
    private String indSector;
    private String indSectorName;
    /**
     * 物料类型
     */
    private String mtlType;
    private String mtlTypeName;
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
}

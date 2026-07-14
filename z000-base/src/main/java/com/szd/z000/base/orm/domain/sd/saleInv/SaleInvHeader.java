package com.szd.z000.base.orm.domain.sd.saleInv;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import lombok.Data;

import java.util.Date;

/**
 * 销售开票-抬头
 */
@Data
public class SaleInvHeader extends ClientWfBussBase {
    /**
     * 开票ID
     */
    private String invId;
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
     * SAP BUDAT
     */
    private String budat;
    /**
     * SAP DOC_TYPE
     */
    private String docType;
    /**
     * SAP开票单号
     */
    private String billDoc;
    /**
     * SAP消息
     */
    private String sapMsg;
    /**
     * SAP消息ID
     */
    private String sapMessId;
    /** 冲销发票号 */
    private String revBillDoc;
    /** 冲销SAP消息 */
    private String revSapMsg;
    /** 冲销SAP消息ID */
    private String revSapMessId;
}

package com.szd.z000.base.orm.domain.mm.purStore;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import lombok.Data;

import java.util.Date;

/**
 * 物料凭证-抬头
 */
@Data
public class PurStoreHeader extends ClientWfBussBase {
    /**
     * 物料凭证ID
     */
    private String storeId;
    /**
     * 单据号[外]
     */
    private String ifDocno;
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
     * 物料凭证
     */
    private String mblnr;
    /**
     * 物料凭证年度
     */
    private String mjahr;

    /**
     * 已创建发票校验数量（展示用，不存库）
     */
    private Integer invCheckCount;
    /**
     * 已创建发票校验金额（展示用，不存库）
     */
    private java.math.BigDecimal invCheckAmt;
    /**
     * SAP已发票校验数量（展示用，不存库）
     */
    private Integer sapInvCheckCount;
    /**
     * SAP已发票校验金额（展示用，不存库）
     */
    private java.math.BigDecimal sapInvCheckAmt;
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
     * 冲销SAP id
     */
    private String revSapMessId;
}

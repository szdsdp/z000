package com.szd.z000.base.orm.domain.mdm.cust;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 客商申请-信用视图
 *
 */
@Data
public class MdmCustCredit {
    /** 客商申请ID */
    private String applyId;
    /** 行号 */
    private int itemNo;
    /** 信用段 */
    private String creditSgmnt;
    private String creditSgmntName;
    /** 信用额度 */
    private BigDecimal creditLimit;
}

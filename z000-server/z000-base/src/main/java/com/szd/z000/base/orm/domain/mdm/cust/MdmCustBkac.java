package com.szd.z000.base.orm.domain.mdm.cust;

import lombok.Data;

/**
 * 客商申请-银行视图
 *
 */
@Data
public class MdmCustBkac {
    /** 客商申请ID */
    private String applyId;
    /** 行号 */
    private int itemNo;
    /** 银行账户 */
    private String acctNo;
    private String acctName;
    /** 联行号 */
    private String cnap;
    private String cnapName;
}

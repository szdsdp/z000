package com.szd.z000.base.orm.domain.mdm.cust;

import lombok.Data;

/**
 * 客商申请-公司视图
 *
 */
@Data
public class MdmCustCmpy {
    /** 客商申请ID */
    private String applyId;
    /** 行号 */
    private int itemNo;
    /** 客商区分(K-客户；D-供应商) */
    private String custFlag;
    /** 所属公司 */
    private String cmpy;
    private String cmpyName;
    /** 统驭科目 */
    private String accs;
    private String accsName;
    /** 付款条件 */
    private String payTerms;
    /** 付款方式 */
    private String payType;
}

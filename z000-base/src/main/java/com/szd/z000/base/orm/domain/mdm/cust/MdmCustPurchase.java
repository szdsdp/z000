package com.szd.z000.base.orm.domain.mdm.cust;

import lombok.Data;

/**
 * 客商申请-采购视图
 *
 */
@Data
public class MdmCustPurchase {
    /** 客商申请ID */
    private String applyId;
    /** 行号 */
    private int itemNo;
    /** 采购组织 */
    private String purchaseOrg;
    private String purchaseOrgName;
    /** 订单货币 */
    private String curr;
    private String currName;
    /** 付款条件 */
    private String payTerms;
    /** 成交方式 */
    private String dealType;
    /** 联系人 */
    private String contact;
    /** 联系电话 */
    private String contactPhone;
}

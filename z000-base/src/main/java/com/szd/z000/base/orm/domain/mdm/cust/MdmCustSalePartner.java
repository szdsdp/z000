package com.szd.z000.base.orm.domain.mdm.cust;

import lombok.Data;

/**
 * 客商申请-销售视图-业务伙伴
 *
 */
@Data
public class MdmCustSalePartner {
    /** 客商申请ID */
    private String applyId;
    /** 行号 */
    private int itemNo;
    /** 销售组织 */
    private String saleOrg;
    /** 订单货币 */
    private String partnerType;
    private String partnerTypeName;
    /** 定价是否含税(1 不含税价; 2 含税价) */
    private String partnerCode;
    private String partnerName;
}

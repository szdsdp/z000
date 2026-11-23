package com.szd.z000.base.orm.domain.sd.saleInv;

import lombok.Data;

/**
 * 销售开票-行项目
 */
@Data
public class SaleInvItem {
    /**
     * 开票ID
     */
    private String invId;
    /**
     * 行项目号
     */
    private int itemNo;
    /**
     * 交货单号
     */
    private String vbelnVl;
    /**
     * 交货单单据号
     */
    private String deliveryBussDocId;
}

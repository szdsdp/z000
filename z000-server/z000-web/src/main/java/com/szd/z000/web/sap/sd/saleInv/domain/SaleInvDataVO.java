package com.szd.z000.web.sap.sd.saleInv.domain;

import com.szd.z000.base.orm.domain.sd.saleInv.SaleInvHeader;
import com.szd.z000.base.orm.domain.sd.saleInv.SaleInvItem;
import lombok.Data;

import java.util.List;

/**
 * 销售开票-数据VO
 */
@Data
public class SaleInvDataVO {
    private SaleInvHeader header;
    private List<SaleInvItem> itemList;
}

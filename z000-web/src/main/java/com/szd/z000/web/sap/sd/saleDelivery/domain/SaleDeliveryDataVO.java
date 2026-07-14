package com.szd.z000.web.sap.sd.saleDelivery.domain;

import com.szd.z000.base.orm.domain.sd.saleDelivery.SaleDeliveryHeader;
import com.szd.z000.base.orm.domain.sd.saleDelivery.SaleDeliveryItem;
import com.szd.z000.base.orm.domain.sd.saleDelivery.SaleDeliveryPost;
import lombok.Data;

import java.util.List;

/**
 * 销售交货单-数据VO
 */
@Data
public class SaleDeliveryDataVO {
    private SaleDeliveryHeader header;
    private List<SaleDeliveryItem> itemList;
    private SaleDeliveryPost post;
}

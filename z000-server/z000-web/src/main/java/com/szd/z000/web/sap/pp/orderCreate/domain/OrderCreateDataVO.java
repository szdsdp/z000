package com.szd.z000.web.sap.pp.orderCreate.domain;

import com.szd.z000.base.orm.domain.pp.orderCreate.OrderCreateHeader;
import lombok.Data;

/**
 * 生产订单创建-数据VO
 */
@Data
public class OrderCreateDataVO {
    /**
     * 生产订单创建-抬头
     */
    private OrderCreateHeader header;
}

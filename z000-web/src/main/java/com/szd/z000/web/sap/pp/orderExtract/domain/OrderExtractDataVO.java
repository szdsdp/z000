package com.szd.z000.web.sap.pp.orderExtract.domain;

import com.szd.z000.base.orm.domain.pp.orderExtract.OrderExtractHeader;
import com.szd.z000.base.orm.domain.pp.orderExtract.OrderExtractModule;
import com.szd.z000.base.orm.domain.pp.orderExtract.OrderExtractProcess;
import lombok.Data;

import java.util.List;

/**
 * 生产订单抽取-数据VO
 */
@Data
public class OrderExtractDataVO {
    private OrderExtractHeader header;
    private List<OrderExtractProcess> processList;
    private List<OrderExtractModule> moduleList;
}

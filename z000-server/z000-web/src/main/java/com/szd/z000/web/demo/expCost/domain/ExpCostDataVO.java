package com.szd.z000.web.demo.expCost.domain;

import com.szd.z000.base.orm.domain.demo.expCost.ExpCostHeader;
import com.szd.z000.base.orm.domain.demo.expCost.ExpCostItem;
import lombok.Data;

import java.util.List;

/**
 * Z1功能业务数据对象
 */
@Data
public class ExpCostDataVO {
    /**
     * 单据抬头
     */
    private ExpCostHeader header;
    /**
     * 单据行项目
     */
    private List<ExpCostItem> itemList;
}

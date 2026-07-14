package com.szd.z000.web.sap.mm.purContract.domain;

import com.szd.z000.base.orm.domain.mm.purContract.PurContractHeader;
import com.szd.z000.base.orm.domain.mm.purContract.PurContractItem;
import lombok.Data;

import java.util.List;

/**
 * 采购合同-数据VO
 */
@Data
public class PurContractDataVO {
    /**
     * 采购合同-抬头
     */
    private PurContractHeader header;
    /**
     * 采购合同-行项目
     */
    private List<PurContractItem> itemList;
}

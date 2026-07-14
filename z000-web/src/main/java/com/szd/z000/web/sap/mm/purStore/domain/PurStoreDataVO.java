package com.szd.z000.web.sap.mm.purStore.domain;

import com.szd.z000.base.orm.domain.mm.purStore.PurStoreHeader;
import com.szd.z000.base.orm.domain.mm.purStore.PurStoreItem;
import com.szd.z000.base.orm.domain.mm.purStore.PurStoreRelateVo;
import lombok.Data;

import java.util.List;

/**
 * 物料凭证-数据VO
 */
@Data
public class PurStoreDataVO {
    /**
     * 物料凭证-抬头
     */
    private PurStoreHeader header;
    /**
     * 物料凭证-行项目
     */
    private List<PurStoreItem> itemList;
    /**
     * 关联单据列表
     */
    private List<PurStoreRelateVo> relateList;
}

package com.szd.z000.web.sap.mm.purApply.domain;

import com.szd.z000.base.orm.domain.mm.purApply.PurApplyHeader;
import com.szd.z000.base.orm.domain.mm.purApply.PurApplyItem;
import com.szd.z000.base.orm.domain.mm.purApply.PurApplyRelateVo;
import lombok.Data;

import java.util.List;

/**
 * 采购申请-数据VO
 */
@Data
public class PurApplyDataVO {
    /**
     * 采购申请-抬头
     */
    private PurApplyHeader header;
    /**
     * 采购申请-行项目
     */
    private List<PurApplyItem> itemList;
    /**
     * 关联单据列表
     */
    private List<PurApplyRelateVo> relateList;
}

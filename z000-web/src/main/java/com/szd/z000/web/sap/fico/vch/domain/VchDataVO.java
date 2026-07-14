package com.szd.z000.web.sap.fico.vch.domain;

import com.szd.z000.base.orm.domain.fico.vch.VchAdjust;
import com.szd.z000.base.orm.domain.fico.vch.VchHeader;
import com.szd.z000.base.orm.domain.fico.vch.VchItem;
import lombok.Data;

import java.util.List;

/**
 * 费用报账-数据VO
 */
@Data
public class VchDataVO {
    /**
     * 抬头
     */
    private VchHeader header;
    /**
     * 行项目
     */
    private List<VchItem> itemList;
    /**
     * 凭证调整分录
     */
    private List<VchAdjust> adjustList;
}

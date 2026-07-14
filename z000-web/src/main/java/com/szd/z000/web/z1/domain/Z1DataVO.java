package com.szd.z000.web.z1.domain;

import com.szd.z000.base.orm.domain.z1.Z1Header;
import com.szd.z000.base.orm.domain.z1.Z1Item;
import lombok.Data;

import java.util.List;

/**
 * Z1功能业务数据对象
 */
@Data
public class Z1DataVO {
    /**
     * 单据抬头
     */
    private Z1Header header;
    /**
     * 单据行项目
     */
    private List<Z1Item> itemList;
}

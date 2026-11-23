package com.szd.z000.web.sap.mm.invCheck.domain;

import com.szd.z000.base.orm.domain.mm.invCheck.InvCheckHeader;
import com.szd.z000.base.orm.domain.mm.invCheck.InvCheckItem;
import lombok.Data;

import java.util.List;

/**
 * 发票校验-数据VO
 */
@Data
public class InvCheckDataVO {
    private InvCheckHeader header;
    private List<InvCheckItem> itemList;
}

package com.szd.z000.web.sap.mm.check.checkTask.domain;

import com.szd.z000.base.orm.domain.mm.check.checkTask.CheckTaskHeader;
import com.szd.z000.base.orm.domain.mm.check.checkTask.CheckTaskItem;
import lombok.Data;

import java.util.List;

/**
 * 盘点任务-数据VO
 */
@Data
public class CheckTaskDataVO {
    private CheckTaskHeader header;
    private List<CheckTaskItem> itemList;
}

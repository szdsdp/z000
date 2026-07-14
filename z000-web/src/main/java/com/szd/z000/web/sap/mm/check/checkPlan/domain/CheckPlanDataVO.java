package com.szd.z000.web.sap.mm.check.checkPlan.domain;

import com.szd.z000.base.orm.domain.mm.check.checkPlan.CheckPlanHeader;
import com.szd.z000.base.orm.domain.mm.check.checkPlan.CheckPlanItem;
import com.szd.z000.base.orm.domain.mm.check.checkPlan.CheckPlanRelateVo;
import com.szd.z000.base.orm.domain.mm.check.checkTask.CheckTaskHeader;
import lombok.Data;

import java.util.List;

/**
 * 库存盘点-数据VO
 */
@Data
public class CheckPlanDataVO {
    /**
     * 库存盘点-抬头
     */
    private CheckPlanHeader header;
    /**
     * 库存盘点-行项目
     */
    private List<CheckPlanItem> itemList;
    /**
     * 关联盘点任务列表
     */
    private List<CheckTaskHeader> taskList;

    // 关联单据
    private List<CheckPlanRelateVo> relateList;
}

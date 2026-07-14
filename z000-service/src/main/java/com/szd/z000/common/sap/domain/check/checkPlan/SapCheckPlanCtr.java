package com.szd.z000.common.sap.domain.check.checkPlan;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 库存盘点创建-请求
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapCheckPlanCtr {
    /**
     * 抬头
     */
    @XmlElement(name = "HEADER")
    private SapCheckPlanCtrHeader header;
    /**
     * 行项目
     */
    @XmlElementWrapper(name = "ITEMS")
    @XmlElement(name = "item")
    private List<SapCheckPlanCtrItem> itemList;
}

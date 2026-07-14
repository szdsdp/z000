package com.szd.z000.common.sap.domain.orderExtract;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 生产订单抽取-返回ITEMS中的单条记录
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapOrderExtractCtrRetItem {
    /** AFKO 订单头(单对象) */
    @XmlElement(name = "AFKO")
    private SapOrderExtractCtrItemAfko afko;

    /** AFVCS 工序(集合) */
    @XmlElementWrapper(name = "AFVCS")
    @XmlElement(name = "item")
    private List<SapOrderExtractCtrItemAfvcs> afvcsList;

    /** RESBS 组件(集合) */
    @XmlElementWrapper(name = "RESBS")
    @XmlElement(name = "item")
    private List<SapOrderExtractCtrItemResbs> resbsList;
}

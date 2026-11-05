package com.szd.z000.common.sap.domain.payRece;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * SAP回款认领单创建-请求根
 */
@Data
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
public class SapPayReceiptCtr {
    /** 抬头 */
    @XmlElement(name = "HEADER")
    private SapPayReceiptCtrHeader header;
    /** 未清项（回款性质10/30使用） */
    @XmlElementWrapper(name = "ITEMS")
    @XmlElement(name = "ITEM")
    private List<SapPayReceiptCtrItem> items;
    /** 客户记账信息（回款性质20/40使用） */
    @XmlElementWrapper(name = "ITEMS2")
    @XmlElement(name = "ITEM")
    private List<SapPayReceiptCtrItem2> items2;
}

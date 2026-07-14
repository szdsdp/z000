package com.szd.z000.common.sap.domain.purOrder;

import lombok.Data;

import javax.xml.bind.annotation.*;
/**
 * 采购订单创建-抬头文本
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "headtxt")
@Data
public class SapPurOrderCtrHeadtxt {
    /** 文本行 */
    @XmlElement(name = "TEXT_LINE")
    private String textLine;
}

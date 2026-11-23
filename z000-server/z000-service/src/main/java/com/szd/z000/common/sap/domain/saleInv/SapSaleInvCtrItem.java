package com.szd.z000.common.sap.domain.saleInv;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 销售开票-行项目
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
@Data
public class SapSaleInvCtrItem {
    /**
     * 交货单号
     */
    @XmlElement(name = "VBELN_VL")
    private String vbelnVl;
}

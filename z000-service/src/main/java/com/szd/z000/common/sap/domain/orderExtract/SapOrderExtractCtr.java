package com.szd.z000.common.sap.domain.orderExtract;

import lombok.Data;

import javax.xml.bind.annotation.*;

/**
 * 生产订单抽取-请求
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapOrderExtractCtr {
    /** 抬头 */
    @XmlElement(name = "HEADER")
    private SapOrderExtractCtrHeader header;
}

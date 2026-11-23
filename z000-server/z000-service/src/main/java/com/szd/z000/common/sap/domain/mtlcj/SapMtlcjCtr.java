package com.szd.z000.common.sap.domain.mtlcj;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 物料主数据创建/修改-请求
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapMtlcjCtr {
    /** 抬头 */
    @XmlElement(name = "HEADER")
    private SapMtlcjCtrHeader header;
    /** 基础视图 */
    @XmlElement(name = "MARA")
    private SapMtlcjCtrMara mara;
    /** 采购视图 */
    @XmlElement(name = "MARC")
    private SapMtlcjCtrMarc marc;
}

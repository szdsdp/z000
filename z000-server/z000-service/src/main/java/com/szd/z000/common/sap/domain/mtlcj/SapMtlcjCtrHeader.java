package com.szd.z000.common.sap.domain.mtlcj;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 物料主数据创建/修改-请求抬头
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "HEADER")
@Data
public class SapMtlcjCtrHeader {
    /** 单据号[外] */
    @XmlElement(name = "IF_DOCNO")
    private String ifDocno;
    /** C创建/U修改 */
    @XmlElement(name = "DTYPE")
    private String dtype;
    /** 物料描述（短文本） */
    @XmlElement(name = "MATL_DESC")
    private String matlDesc;
    /** 物料编号（修改时必传） */
    @XmlElement(name = "MATERIAL")
    private String material;
    /** 行业领域 */
    @XmlElement(name = "IND_SECTOR")
    private String indSector;
    /** 物料类型 */
    @XmlElement(name = "MATL_TYPE")
    private String matlType;
    /** 基本数据视图 */
    @XmlElement(name = "BASIC_VIEW")
    private String basicView;
    /** 采购视图 */
    @XmlElement(name = "PURCHASE_VIEW")
    private String purchaseView;
}

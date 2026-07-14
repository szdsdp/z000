package com.szd.z000.common.sap.domain.cust;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 客户维护-抬头
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "HEADER")
@Data
public class SapCustCtrHeader {
    /** 客商编码 */
    @XmlElement(name = "PARTNER")
    private String custId;
    /** 客商名称 */
    @XmlElement(name = "NAME1")
    private String custName;
    /** U8旧编码 */
    @XmlElement(name = "NAME3")
    private String u8Code;
    /** 客商简称 */
    @XmlElement(name = "MCOD1")
    private String custNameShort;
    /** 伙伴类型 */
    @XmlElement(name = "BPKIND")
    private String partnerType;
    /** 客商分组 */
    @XmlElement(name = "BU_GROUP")
    private String  custGrp;
    /** 国家 */
    @XmlElement(name = "LAND1")
    private String ctry;
    /** 省 */
    @XmlElement(name = "REGIO")
    private String province;
    /** 城市 */
    @XmlElement(name = "ORT01")
    private String city;
    /** 街道 */
    @XmlElement(name = "STRAS")
    private String address;
    /** 电话 */
    @XmlElement(name = "TELF1")
    private String phone;
    /** 联系人 */
    @XmlElement(name = "REMARK")
    private String contact;
    /** 税号 */
    @XmlElement(name = "STCEG")
    private String idNum;
    /** 供应商标识 */
    @XmlElement(name = "LIFNR_F")
    private String custFlagD;
    /** 客户标识 */
    @XmlElement(name = "KUNNR_F")
    private String custFlagK;
    /** 风险类 */
    @XmlElement(name = "RISK_CLASS")
    private String riskClass;
    /** 客户税分类 */
    @XmlElement(name = "TAXKD")
    private String taxClass;
    /** 销售大区 */
    @XmlElement(name = "KVGR1")
    private String saleRegion;

    /** 邮政编码 */
    @XmlElement(name = "PSTLZ")
    private String postCode;
}

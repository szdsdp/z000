package com.szd.z000.common.sap.domain.vch;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * SAP会计凭证生成-行项目
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class SapVchCtrItem {
    /** 会计凭证中的行项目数 */
    @XmlElement(name = "BUZEI")
    private String buzei;
    /** 记帐代码 */
    @XmlElement(name = "BSCHL")
    private String bschl;
    /** 借方/贷方标识 */
    @XmlElement(name = "SHKZG")
    private String shkzg;
    /** 凭证货币金额 */
    @XmlElement(name = "WRBTR")
    private String wrbtr;
    /** 按本位币计的金额 */
    @XmlElement(name = "DMBTR")
    private String dmbtr;
    /** 供应商或债权人的帐号 */
    @XmlElement(name = "LIFNR")
    private String lifnr;
    /** 客户编号 */
    @XmlElement(name = "KUNNR")
    private String kunnr;
    /** 总账科目 */
    @XmlElement(name = "HKONT")
    private String hkont;
    /** 特殊总帐标识 */
    @XmlElement(name = "UMSKZ")
    private String umskz;
    /** 分配编号 */
    @XmlElement(name = "ZUONR")
    private String zuonr;
    /** 项目文本 */
    @XmlElement(name = "SGTXT")
    private String sgtxt;
    /** 成本中心 */
    @XmlElement(name = "KOSTL")
    private String kostl;
    /** 业务范围 */
    @XmlElement(name = "GSBER")
    private String gsber;
    /** 利润中心 */
    @XmlElement(name = "PRCTR")
    private String prctr;
    /** 部分报表分段 */
    @XmlElement(name = "SEGMENT")
    private String segment;
    /** 销售/购买税代码 */
    @XmlElement(name = "MWSKZ")
    private String mwskz;
    /** 付款原因代码 */
    @XmlElement(name = "RSTGR")
    private String rstgr;
    /** 采购凭证号 */
    @XmlElement(name = "EBELN")
    private String ebeln;
    /** 采购凭证的项目编号 */
    @XmlElement(name = "EBELP")
    private String ebelp;
    /** 主资产号 */
    @XmlElement(name = "ANLN1")
    private String anln1;
    /** 资产次级编号 */
    @XmlElement(name = "ANLN2")
    private String anln2;
    /** 订单号 */
    @XmlElement(name = "AUFNR")
    private String aufnr;
    /** 付款条件代码 */
    @XmlElement(name = "ZTERM")
    private String zterm;
    /** 用于到期日计算的基准日期 */
    @XmlElement(name = "ZFBDT")
    private String zfbdt;
    /** 付款方式 */
    @XmlElement(name = "ZLSCH")
    private String zlsch;
    /** 收付冻结码 */
    @XmlElement(name = "ZLSPR")
    private String zlspr;
    /** 标识: 反记帐 */
    @XmlElement(name = "XNEGP")
    private String xnegp;
}

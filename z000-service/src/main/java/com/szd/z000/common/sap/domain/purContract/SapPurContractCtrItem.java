package com.szd.z000.common.sap.domain.purContract;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

/**
 * 采购合同创建-行项目
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
@Data
public class SapPurContractCtrItem {
    /** 采购凭证的项目编号 */
    @XmlElement(name = "ITEM_NO")
    private String itemNo;
    /** 短文本 */
    @XmlElement(name = "SHORT_TEXT")
    private String shortText;
    /** 物料编号 */
    @XmlElement(name = "MATERIAL")
    private String material;
    /** 工厂 */
    @XmlElement(name = "PLANT")
    private String plant;
    /** 库存地点 */
    @XmlElement(name = "STGE_LOC")
    private String stgeLoc;
    /** 库存运输订单发货存储地点 */
    @XmlElement(name = "SUPPL_STLOC")
    private String supplStloc;
    /** 需求跟踪号 */
    @XmlElement(name = "TRACKINGNO")
    private String trackingno;
    /** 物料组 */
    @XmlElement(name = "MATL_GROUP")
    private String matlGroup;
    /** 采购信息记录的编号 */
    @XmlElement(name = "INFO_REC")
    private String infoRec;
    /** 供应商使用的物料编号 */
    @XmlElement(name = "VEND_MAT")
    private String vendMat;
    /** 目标数量 */
    @XmlElement(name = "TARGET_QTY")
    private BigDecimal targetQty;
    /** 订单单位 */
    @XmlElement(name = "PO_UNIT")
    private String poUnit;
    /** 净价 */
    @XmlElement(name = "NET_PRICE")
    private BigDecimal netPrice;
    /** 价格单位 */
    @XmlElement(name = "PRICE_UNIT")
    private BigDecimal priceUnit;
    /** 销售/购买税代码 */
    @XmlElement(name = "TAX_CODE")
    private String taxCode;
    /** 库存类型 */
    @XmlElement(name = "QUAL_INSP")
    private String qualInsp;
    /** 评估类型 */
    @XmlElement(name = "VAL_TYPE")
    private String valType;
    /** 交货已完成标识 */
    @XmlElement(name = "NO_MORE_GR")
    private String noMoreGr;
    /** 最后发票标识 */
    @XmlElement(name = "FINAL_INV")
    private String finalInv;
    /** 项目类别 */
    @XmlElement(name = "ITEM_CAT")
    private String itemCat;
    /** 科目分配类别 */
    @XmlElement(name = "ACCTASSCAT")
    private String acctasscat;
}

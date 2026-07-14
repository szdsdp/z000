package com.szd.z000.common.sap.domain.invCheck;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 发票校验-行项目(请求)
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapInvCheckCtrItem {
    /** 发票凭证项目 */
    @XmlElement(name = "INVOICE_DOC_ITEM")
    private String invoiceDocItem;
    /** 采购订单编号 */
    @XmlElement(name = "PO_NUMBER")
    private String poNumber;
    /** 采购凭证项目编号 */
    @XmlElement(name = "PO_ITEM")
    private String poItem;
    /** 税码 */
    @XmlElement(name = "TAX_CODE")
    private String taxCode;
    /** 凭证货币金额 */
    @XmlElement(name = "ITEM_AMOUNT")
    private String itemAmount;
    /** 税额 */
    @XmlElement(name = "TAX_AMOUNT")
    private String taxAmount;
    /** 数量 */
    @XmlElement(name = "QUANTITY")
    private String quantity;
    /** 订单单位 */
    @XmlElement(name = "PO_UNIT")
    private String poUnit;
    /** 项目文本 */
    @XmlElement(name = "ITEM_TEXT")
    private String itemText;
}

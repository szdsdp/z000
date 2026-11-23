package com.szd.z000.common.sap.domain.payRece;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * SAP回款认领单创建-未清项（回款性质10/30使用）
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class SapPayReceiptCtrItem {
    /** 会计凭证编号 */
    @XmlElement(name = "BELNR")
    private String belnr;
    /** 财年 */
    @XmlElement(name = "GJAHR")
    private String gjahr;
    /** 会计凭证中的行项目数 */
    @XmlElement(name = "BUZEI")
    private String buzei;
    /** 清账金额 */
    @XmlElement(name = "WRBTR")
    private String wrbtr;
}

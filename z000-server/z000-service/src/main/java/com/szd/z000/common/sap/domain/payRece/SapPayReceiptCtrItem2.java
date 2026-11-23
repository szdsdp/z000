package com.szd.z000.common.sap.domain.payRece;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * SAP回款认领单创建-客户记账信息（回款性质20/40使用）
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class SapPayReceiptCtrItem2 {
    /** 客户编号 */
    @XmlElement(name = "DF_KUNNR")
    private String dfKunnr;
    /** 项目文本 */
    @XmlElement(name = "DF_SGTXT")
    private String dfSgtxt;
    /** 凭证货币金额 */
    @XmlElement(name = "DF_WRBTR")
    private String dfWrbtr;
}

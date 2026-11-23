package com.szd.z000.common.sap.domain.purStore;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 物料凭证冲销-请求
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapPurStoreReverseCtr {
    /** 物料凭证编号 */
    @XmlElement(name = "MAT_DOC")
    private String matDoc;
    /** 物料凭证年度 */
    @XmlElement(name = "DOC_YEAR")
    private String docYear;
    /** 凭证中的过帐日期 */
    @XmlElement(name = "BUDAT")
    private String budat;
}

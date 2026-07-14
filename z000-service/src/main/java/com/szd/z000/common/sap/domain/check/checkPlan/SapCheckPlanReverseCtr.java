package com.szd.z000.common.sap.domain.check.checkPlan;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 库存盘点冲销-请求
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapCheckPlanReverseCtr {
    @XmlElement(name = "MAT_DOC")
    private String matDoc;
    @XmlElement(name = "DOC_YEAR")
    private String docYear;
    @XmlElement(name = "BUDAT")
    private String budat;
}

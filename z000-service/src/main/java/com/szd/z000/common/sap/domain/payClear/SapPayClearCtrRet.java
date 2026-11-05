package com.szd.z000.common.sap.domain.payClear;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 客商未清项抽取-返回
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapPayClearCtrRet {
    /** 未清项清单 */
    @XmlElementWrapper(name = "ITEMS")
    @XmlElement(name = "item")
    private List<SapPayClearCtrRetItem> items;
}

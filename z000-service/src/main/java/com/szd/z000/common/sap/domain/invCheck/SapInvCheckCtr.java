package com.szd.z000.common.sap.domain.invCheck;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 发票校验-请求
 */
@XmlRootElement(name = "DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SapInvCheckCtr {
    @XmlElement(name = "HEADER")
    private SapInvCheckCtrHeader header;
    @XmlElementWrapper(name = "ITEMS")
    @XmlElement(name = "item")
    private List<SapInvCheckCtrItem> itemList;
}

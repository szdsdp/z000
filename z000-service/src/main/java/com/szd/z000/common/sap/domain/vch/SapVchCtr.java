package com.szd.z000.common.sap.domain.vch;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * SAP会计凭证生成-请求根
 */
@Data
@XmlRootElement(name = "ROOT")
@XmlAccessorType(XmlAccessType.FIELD)
public class SapVchCtr {
    @XmlElement(name = "HEADER")
    private SapVchCtrHeader header;
    @XmlElementWrapper(name = "ITEMS")
    @XmlElement(name = "ITEM")
    private List<SapVchCtrItem> itemList;
}

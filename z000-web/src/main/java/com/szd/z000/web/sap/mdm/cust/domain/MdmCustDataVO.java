package com.szd.z000.web.sap.mdm.cust.domain;

import com.szd.z000.base.orm.domain.mdm.cust.*;
import lombok.Data;

import java.util.List;

@Data
public class MdmCustDataVO {
    // 客商申请-抬头
    private MdmCustHeader header;
    // 客商申请-公司视图-客户
    private List<MdmCustCmpy> cmpyKList;
    // 客商申请-公司视图-供应商
    private List<MdmCustCmpy> cmpyDList;
    // 客商申请-银行视图
    private List<MdmCustBkac> bkacList;
    // 客商申请-销售视图
    private List<MdmCustSale> saleList;
    // 客商申请-采购视图
    private List<MdmCustPurchase> purchaseList;
    // 客商申请-信用视图
    private List<MdmCustCredit> creditList;
}

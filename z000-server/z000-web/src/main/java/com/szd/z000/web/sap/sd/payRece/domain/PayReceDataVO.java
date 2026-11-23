package com.szd.z000.web.sap.sd.payRece.domain;

import com.szd.z000.base.orm.domain.sd.payRece.PayReceiptBill;
import com.szd.z000.base.orm.domain.sd.payRece.PayReceiptCash;
import com.szd.z000.base.orm.domain.sd.payRece.PayReceiptHeader;
import com.szd.z000.base.orm.domain.sd.payRece.PayReceiptItem;
import lombok.Data;

import java.util.List;

/**
 * 回款认领数据对象
 */
@Data
public class PayReceDataVO {
    /**
     * 抬头
     */
    private PayReceiptHeader header;
    /**
     * 电汇信息
     */
    private PayReceiptCash cash;
    /**
     * 票据信息
     */
    private PayReceiptBill bill;
    /**
     * 行项目
     */
    private List<PayReceiptItem> itemList;
}

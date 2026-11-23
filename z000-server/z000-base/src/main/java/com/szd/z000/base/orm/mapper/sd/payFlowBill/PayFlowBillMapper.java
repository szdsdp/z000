package com.szd.z000.base.orm.mapper.sd.payFlowBill;

import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.z000.base.orm.domain.sd.payFlowBill.PayFlowBill;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 票据承兑Mapper
 */
public interface PayFlowBillMapper {
    List<PayFlowBill> selectList(ClientRpParam param);
    PayFlowBill selectById(String sysBillNo);
    PayFlowBill selectByUk(@Param("holderAcc") String holderAcc, @Param("billNo") String billNo, @Param("subBillFrom") String subBillFrom, @Param("subBillTo") String subBillTo);
    void insert(PayFlowBill bill);
    void batchInsert(List<PayFlowBill> list);
    void update(PayFlowBill bill);
    void batchUpdate(List<PayFlowBill> list);
    void updateCust(@Param("sysBillNo") List<String> list, @Param("cust") String cust);
    void updateAmtReceipt(@Param("bankDoc") String bankDoc, @Param("amtProcess") BigDecimal amtProcess, @Param("amtReceipt") BigDecimal amtReceipt);
    void deleteById(String sysBillNo);
}

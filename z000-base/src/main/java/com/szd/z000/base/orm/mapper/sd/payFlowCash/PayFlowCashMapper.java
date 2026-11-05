package com.szd.z000.base.orm.mapper.sd.payFlowCash;

import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.z000.base.orm.domain.sd.payFlowCash.PayFlowCash;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 电汇回款Mapper
 */
public interface PayFlowCashMapper {
    List<PayFlowCash> selectList(ClientRpParam param);
    PayFlowCash selectById(String bankDoc);
    PayFlowCash selectByParams(@Param("bankFlowNo") String bankFlowNo, @Param("myAccNo") String myAccNo);
    void insert(PayFlowCash entity);
    void batchInsert(List<PayFlowCash> list);
    void update(PayFlowCash entity);
    void batchUpdate(List<PayFlowCash> list);
    void updateCust(@Param("bankDoc") List<String> bankDocList, @Param("cust") String cust);
    void updateAmtReceipt(@Param("bankDoc") String bankDoc, @Param("amtProcess") BigDecimal amtProcess, @Param("amtReceipt") BigDecimal amtReceipt);
    void deleteById(String bankDoc);
}

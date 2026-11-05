package com.szd.z000.base.orm.mapper.sd.payRece;

import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.z000.base.orm.domain.sd.payRece.PayReceiptHeader;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 回款认领-头Mapper
 */
public interface PayReceiptHeaderMapper {
    List<PayReceiptHeader> selectList(ClientRpParam param);
    PayReceiptHeader selectById(String receiptId);
    int insert(PayReceiptHeader header);
    int update(PayReceiptHeader header);
    void updateStatusAndDocId(@Param("bussId") String bussId, @Param("bussStatus") String bussStatus, @Param("bussDocId") String bussDocId);
    void updateSapMsg(@Param("receiptId") String receiptId, @Param("sapMessId") String sapMessId, @Param("sapMsg") String sapMsg);
    void updateSapVchInfo(@Param("receiptId") String receiptId, @Param("vchNum") String vchNum, @Param("vchYear") String vchYear, @Param("sapMessId") String sapMessId, @Param("sapMsg") String sapMsg);
    void updateSapReverseVchInfo(@Param("receiptId") String receiptId, @Param("vchNumReverse") String vchNumReverse, @Param("vchYearReverse") String vchYearReverse, @Param("sapMessId") String sapMessId, @Param("sapMsg") String sapMsg);
    int deleteById(String receiptId);
    BigDecimal getSumAmtReceipt(String bankDoc);
    BigDecimal getSumAmtProcess(String bankDoc);
}

package com.szd.z000.base.orm.mapper.sd.payRece;

import com.szd.z000.base.orm.domain.sd.payRece.PayReceiptItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 回款认领-行项目Mapper
 */
public interface PayReceiptItemMapper {
    List<PayReceiptItem> selectList(@Param("receiptId") String receiptId);
    int insert(PayReceiptItem item);
    int deleteById(String receiptId);
    int batchInsert(@Param("receiptId") String receiptId, @Param("list") List<PayReceiptItem> list);
}

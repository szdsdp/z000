package com.szd.z000.base.orm.mapper.sd.saleInv;

import com.szd.z000.base.orm.domain.sd.saleInv.SaleInvItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 销售开票-行项目Mapper
 */
public interface SaleInvItemMapper {
    List<SaleInvItem> selectList(String invId);
    void batchInsert(List<SaleInvItem> list);
    void deleteByInvId(String invId);
    /**
     * 查询已开票的交货单号（排除指定开票单，状态非E即为有效）
     */
    List<String> selectInvoicedVbelnVl(@Param("vbelnVlList") List<String> vbelnVlList, @Param("excludeInvId") String excludeInvId);
}

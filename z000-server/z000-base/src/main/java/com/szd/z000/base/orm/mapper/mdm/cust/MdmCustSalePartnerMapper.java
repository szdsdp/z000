package com.szd.z000.base.orm.mapper.mdm.cust;

import com.szd.z000.base.orm.domain.mdm.cust.MdmCustSalePartner;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客商申请-销售视图-业务伙伴
 */
public interface MdmCustSalePartnerMapper {
    List<MdmCustSalePartner> selectList(@Param("applyId") String applyId, @Param("saleOrg") String saleOrg);
    void batchInsert(List<MdmCustSalePartner> list);
    void deleteById(@Param("applyId") String applyId, @Param("saleOrg") String saleOrg);
}

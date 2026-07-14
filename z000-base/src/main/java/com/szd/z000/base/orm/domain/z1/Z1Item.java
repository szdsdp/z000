package com.szd.z000.base.orm.domain.z1;

import lombok.Data;

import java.math.BigDecimal;

/**
 * SDEM1-行项目
 *
 * @author ADMIN
 */
@Data
public class Z1Item {
    /** ID */
    private String bussId;
    /** 行项目 */
    private Integer itemNo;
    private String bsub;
    private String bsubName;
    /** 会计科目 */
    private String accs;
    private String accsName;
    /** 税率 */
    private BigDecimal taxr;
    /** 金额 */
    private BigDecimal amt;
    private BigDecimal amtTax;
    private BigDecimal amtNoTax;
    /** 供应商 */
    private String supp;
    private String suppName;
    /** 客户 */
    private String cust;
    private String custName;
    /** 特别总账标识 */
    private String sgl;
    /** 摘要 */
    private String summary;
    /** 成本中心 */
    private String cstc;
    private String cstcName;
    /** 利润中心 */
    private String prfc;
    private String prfcName;
    private String remark;
}

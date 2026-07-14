package com.szd.z000.base.orm.domain.demo.expCost;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 行项目
 *
 * @author ADMIN
 */
@Data
public class ExpCostItem {
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
    private String remark;
}

package com.szd.z000.base.orm.domain.fico.vch;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 费用报账-行项目
 */
@Data
public class VchItem {
    /**
     * 报账单ID
     */
    private String costId;
    /**
     * 行号
     */
    private int itemNo;
    /**
     * 业务小类
     */
    private String bsubl;
    private String bsublName;
    /**
     * 抵扣
     */
    private String deduFlag;
    /**
     * 税率
     */
    private String taxr;
    private String taxrName;
    /**
     * 含税金额
     */
    private BigDecimal amt;
    /**
     * 税额
     */
    private BigDecimal amtTax;
    /**
     * 不含税金额
     */
    private BigDecimal amtNoTax;
}

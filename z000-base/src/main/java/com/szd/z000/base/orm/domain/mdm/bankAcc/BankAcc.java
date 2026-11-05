package com.szd.z000.base.orm.domain.mdm.bankAcc;

import com.szd.core.client.domain.common.ClientPageEntity;
import com.szd.z000.base.common.excel.ExcelExport;
import lombok.Data;

/**
 * 本方账户
 */
@Data
public class BankAcc extends ClientPageEntity {
    /**
     * 银行账户
     */
    @ExcelExport(name = "银行账户")
    private String accNo;

    /**
     * 账户名称
     */
    @ExcelExport(name = "账户名称")
    private String accName;

    /**
     * 币种
     */
    @ExcelExport(name = "币种")
    private String curr;
    private String currName;

    /**
     * 公司代码
     */
    @ExcelExport(name = "公司代码")
    private String cmpy;
    private String cmpyName;

    /**
     * 会计科目
     */
    @ExcelExport(name = "会计科目")
    private String accs;
    private String accsName;

    /**
     * 联行号
     */
    @ExcelExport(name = "联行号")
    private String bankCode;
}

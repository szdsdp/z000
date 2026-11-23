package com.szd.z000.base.orm.domain.fico.vch;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 费用报账-凭证调整分录
 */
@Data
public class VchAdjust {
    /**
     * 报账单ID
     */
    private String costId;
    /**
     * 行号
     */
    private int itemNo;
    /**
     * 会计科目
     */
    private String accs;
    private String accsName;
    /**
     * 借贷(S借 H贷)
     */
    private String drCr;
    /**
     * 金额
     */
    private BigDecimal amt;
    /**
     * 供应商
     */
    private String supp;
    private String suppName;
    /**
     * 客户
     */
    private String cust;
    private String custName;
    /**
     * 特别总账标识
     */
    private String sgl;
    /**
     * 摘要
     */
    private String summary;
    /**
     * 成本中心
     */
    private String cstc;
    private String cstcName;
    /**
     * 利润中心
     */
    private String prfc;
    private String prfcName;
    /**
     * 业务范围
     */
    private String busc;
    private String buscName;
    /**
     * 分配编号
     */
    private String assignNo;
    /**
     * 原因代码
     */
    private String reas;
    private String reasName;
    /**
     * 采购凭证
     */
    private String poDoc;
    /**
     * 采购凭证项目
     */
    private int poDocItem;
    /**
     * 内部订单
     */
    private String orderCode;
    private String orderName;
    /**
     * 段
     */
    private String segm;
    private String segmName;
    /**
     * 功能范围
     */
    private String fusc;
    private String fuscName;
    /**
     * 固定资产
     */
    private String assetNo;
    /**
     * 资产次级编号
     */
    private String assetNoSub;
    /**
     * WBS项目号
     */
    private String wbsNo;
}

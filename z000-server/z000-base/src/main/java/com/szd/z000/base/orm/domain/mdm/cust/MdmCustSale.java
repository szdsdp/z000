package com.szd.z000.base.orm.domain.mdm.cust;

import lombok.Data;

import java.util.List;

/**
 * 客商申请-销售视图
 *
 */
@Data
public class MdmCustSale {
    /** 客商申请ID */
    private String applyId;
    /** 行号 */
    private int itemNo;
    /** 销售组织 */
    private String saleOrg;
    private String saleOrgName;
    /** 分销渠道 */
    private String vtweg;
    /** 产品组 */
    private String spart;
    /** 订单货币 */
    private String curr;
    private String currName;
    /** 定价是否含税(1 不含税价; 2 含税价) */
    private String priceTax;
    /** 客户分类 */
    private String custClass;
    /** 客户二级分类 */
    private String custClassSub;
    /** 付款条件 */
    private String payTerms;
    /** 科目分配组 */
    private String accsGrp;
    /** 国际贸易条款 */
    private String incoterms;
    /** 国际贸易条款1 */
    private String incoterms1;
    /** 销售部门 */
    private String saleDept;
    private String saleDeptName;
    /** 销售大区 */
    private String saleRegion;
    private String saleRegionName;
    /** 装运条件 */
    private String shippingTerms;
    /** 信用控制范围 */
    private String creditCtrlArea;
    /** 业务伙伴 */
    private List<MdmCustSalePartner> partnerList;
}

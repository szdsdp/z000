package com.szd.z000.base.orm.domain.mdm.mtlcj;

import lombok.Data;

/**
 * 物料主数据创建申请-采购视图
 */
@Data
public class MtlcjPurView {
    /** 申请ID */
    private String applyId;
    /** 工厂 */
    private String fact;
    private String factName;
    /** ABC标识 */
    private String abcId;
    private String abcIdName;
    /** 采购组 */
    private String purGroup;
    private String purGroupName;
    /** 发货单位 */
    private String issueUnit;
    private String issueUnitName;
    /** 安全库存 */
    private String safetyStk;
    /** 最小批量 */
    private String minLotsize;
    /** 最大批量大小 */
    private String maxLotsize;
    /** 固定批量大小 */
    private String fixedLot;
    /** 采购订单数量的舍入值 */
    private String roundVal;
    /** 最大库存水平 */
    private String maxStock;
}

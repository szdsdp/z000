package com.szd.z000.base.orm.domain.mdm.mtlcj;

import lombok.Data;

/**
 * 物料主数据创建申请-基础视图
 */
@Data
public class MtlcjBaseView {
    /** 申请ID */
    private String applyId;
    /** 基本计量单位 */
    private String unitBase;
    private String unitBaseName;
    /** 物料组 */
    private String mtlGroup;
    private String mtlGroupName;
    /** 大小/量纲 */
    private String sizeDim;
    /** 订单单位 */
    private String unitPo;
    private String unitPoName;
    /** 净重 */
    private String netWt;
    /** 重量单位 */
    private String unitWt;
    private String unitWtName;
}

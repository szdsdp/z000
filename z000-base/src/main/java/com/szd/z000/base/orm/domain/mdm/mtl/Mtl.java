package com.szd.z000.base.orm.domain.mdm.mtl;

import lombok.Data;

/**
 * 物料主数据
 */
@Data
public class Mtl {
    /** 物料编码 */
    private String mtlCode;
    /** 物料名称 */
    private String mtlName;
    /** 基本单位 */
    private String unit;
    private String unitName;
    /** 物料类型 */
    private String mtlType;
    private String mtlTypeName;
    /** 产品组 */
    private String prodGroup;
    private String prodGroupName;
    /** 物料组 */
    private String mtlGroup;
    private String mtlGroupName;
}

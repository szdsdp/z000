package com.szd.z000.base.orm.domain.mm.mtlStock;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存查询-库存数据
 */
@Data
public class MtlStock {
    /** 物料编号 */
    private String matnr;
    /** 物料描述 */
    private String maktx;
    /** 工厂 */
    private String werks;
    /** 工厂名称 */
    private String werksName;
    /** 库存地点 */
    private String lgort;
    /** 库存地点名称 */
    private String lgortName;
    /** 非限制库存 */
    private BigDecimal labst;
    /** 冻结库存 */
    private BigDecimal speme;
    /** 单位 */
    private String unit;
    /** 单位名称 */
    private String unitName;
    /** 创建人 */
    private String createBy;
    /** 创建时间 */
    private Date createTime;
}

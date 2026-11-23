package com.szd.z000.base.orm.domain.mm.purApply;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购申请-行项目
 */
@Data
public class PurApplyItem {
    /**
     * 申请ID
     */
    private String applyId;
    /**
     * 行号
     */
    private int itemNo;
    /**
     * 采购组
     */
    private String purGroup;
    private String purGroupName;
    /**
     * 需求者姓名
     */
    private String needUser;
    /**
     * 短文本
     */
    private String itemText;
    /**
     * 物料编码
     */
    private String mtlCode;
    private String mtlName;
    /**
     * 工厂
     */
    private String fact;
    private String factName;
    /**
     * 库存地点
     */
    private String store;
    private String storeName;
    /**
     * 需求跟踪号
     */
    private String needNo;
    /**
     * 物料组
     */
    private String mtlGroup;
    private String mtlGroupName;
    /**
     * 采购申请数量
     */
    private BigDecimal applyNum;
    /**
     * 单位
     */
    private String unit;
    /**
     * 需求日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date needDate;
    /**
     * 交货日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date deliveryDate;
}

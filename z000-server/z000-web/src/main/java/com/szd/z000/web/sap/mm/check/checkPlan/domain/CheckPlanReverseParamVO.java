package com.szd.z000.web.sap.mm.check.checkPlan.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 库存盘点-冲销参数VO
 */
@Data
public class CheckPlanReverseParamVO {
    private String planId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date budat;
}

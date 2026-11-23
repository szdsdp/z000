package com.szd.z000.web.sap.mm.purStore.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 物料凭证-冲销请求参数
 */
@Data
public class PurStoreReverseParamVO {
    /**
     * 物料凭证ID
     */
    private String storeId;
    /**
     * 冲销日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date budat;
}

package com.szd.z000.base.orm.domain.pp.orderExtract;

import lombok.Data;

/**
 * 生产订单抽取-工序
 */
@Data
public class OrderExtractProcess {
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 行号
     */
    private Integer itemNo;
    /**
     * 工艺路线编号
     */
    private String routingNo;
    /**
     * 工序内部编号
     */
    private String operInnerNo;
    /**
     * 工序编号
     */
    private String operNo;
    /**
     * 标准文本码
     */
    private String stdTextCode;
    /**
     * 工序描述
     */
    private String operDesc;
}

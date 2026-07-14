package com.szd.z000.common.sap.domain.purOrderSend;

import lombok.Data;

import java.util.List;

/**
 * 接收从WMS传入的设备档案数据
 */
@Data
public class PurOrderSendAdd {
    /**
     * 数据类型
     */
    private String dtype;
    private List<PurOrderSendAddData> items;
}

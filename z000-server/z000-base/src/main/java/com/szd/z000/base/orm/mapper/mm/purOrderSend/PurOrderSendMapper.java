package com.szd.z000.base.orm.mapper.mm.purOrderSend;

import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.z000.base.orm.domain.mm.purOrderSend.PurOrderSend;

import java.util.List;

/**
 * 采购订单推送接收
 */
public interface PurOrderSendMapper {
    List<PurOrderSend> selectList(ClientRpParam param);
    void insert(PurOrderSend entity);
}

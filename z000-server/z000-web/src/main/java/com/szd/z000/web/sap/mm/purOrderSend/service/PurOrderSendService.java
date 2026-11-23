package com.szd.z000.web.sap.mm.purOrderSend.service;

import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.z000.base.orm.domain.mm.purOrderSend.PurOrderSend;
import com.szd.z000.base.orm.mapper.mm.purOrderSend.PurOrderSendMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 采购订单推送接收
 */
@Service
public class PurOrderSendService {
    @Resource
    private PurOrderSendMapper purOrderSendMapper;

    public List<PurOrderSend> selectList(ClientRpParam param) {
        return purOrderSendMapper.selectList(param);
    }

    public void insert(PurOrderSend entity) {
        purOrderSendMapper.insert(entity);
    }
}

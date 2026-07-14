package com.szd.z000.common.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.service.ClientCoreFComInf;
import com.szd.z000.base.common.constant.SapConstant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 通用接口服务
 */
@Service
public class CommonInfService implements ClientCoreFComInf {

//    @Resource
//    private SapCgddSendService sapCgddSendService;

    @Override
    public String comInfMethod(ClientDestination destination, JSONObject sourceBody) {

        switch (destination.getBussTp()) {
            // 采购订单推送
            case SapConstant.SZDMM001:
//                return sapCgddSendService.save(destination, sourceBody);
            default:
                destination.setStatus("E");
                destination.setMessText("业务类型不匹配");
        }
        return JSON.toJSONString(destination);
    }
}

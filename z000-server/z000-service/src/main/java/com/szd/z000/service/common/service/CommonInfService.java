package com.szd.z000.service.common.service;

import com.alibaba.fastjson.JSONObject;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.core.client.service.ClientCoreFComInf;
import org.springframework.stereotype.Service;

/**
 * 通用接口服务
 */
@Service
public class CommonInfService implements ClientCoreFComInf {

    @Override
    public String comInfMethod(ClientDestination destination, JSONObject sourceBody) {

        return "";
    }
}

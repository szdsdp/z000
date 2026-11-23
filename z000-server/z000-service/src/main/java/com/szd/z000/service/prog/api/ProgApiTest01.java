package com.szd.z000.service.prog.api;

import com.alibaba.fastjson.JSONObject;
import com.szd.core.client.annotation.ClientProgApi;
import com.szd.core.client.annotation.ClientProgApiInf;
import com.szd.core.client.domain.common.ClientApiEntity;
import com.szd.core.client.domain.common.ClientApiHead;
import com.szd.core.client.domain.wf.*;
import org.springframework.stereotype.Service;

/**
 * 作业程序-测试01
 */
@Service
@ClientProgApi(proCode = "Z000_API_001", proName = "功能描述xxx")
public class ProgApiTest01 implements ClientProgApiInf {
    @Override
    public ClientApiEntity process(ClientApiEntity clientApiEntity) {
        JSONObject retParam = new JSONObject();
        ClientApiHead head = clientApiEntity.getHead();
        JSONObject inputParam = clientApiEntity.getBody();
        try {
            // 业务逻辑处理

            // 处理完返回结果
            // retParam 赋值

        } catch (Exception e) {

        }
        return new ClientApiEntity(head, retParam);
    }
}

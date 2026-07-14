package com.szd.z000.service.common.utils;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import com.szd.core.client.domain.wf.ClientWfParamBuss;
import com.szd.core.client.domain.wf.ClientWfResult;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.util.ClientUtilReflect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 业务主体
 *
 * @author Dave
 */
@Service
public class Z000UtilBussBaseService {
    private static final Logger logger = LoggerFactory.getLogger(Z000UtilBussBaseService.class);
    @Resource
    private ClientCore clientCore;

    /**
     * 保存业务主体
     */
    public void saveBussBase(Object header) {
        ClientWfBussBase wfBussBase = new ClientWfBussBase();
        BeanUtils.copyProperties(header, wfBussBase);
        ClientWfResult clientWfResult = clientCore.wf.baseSave(wfBussBase);
        if (clientWfResult != null && clientWfResult.getRetCode().startsWith("E")) {
            throw new ClientCustomException(clientWfResult.getRetCodeDesc());
        }
    }

    /**
     * 设置业务参数
     */
    public ClientWfParamBuss setWfParamBuss(Object header, String bussId) {
        ClientWfParamBuss wfParamBuss = new ClientWfParamBuss();
        Map<String, String> wfFields = new HashMap<>(16);
        Map<String, String> stringStringMap = clientCore.wf.wfSetField(header);
        if (!CollectionUtils.isEmpty(stringStringMap)) {
            for (String code : stringStringMap.keySet()) {
                Object fieldValue = ClientUtilReflect.getFieldValue(header, code);
                if (fieldValue != null) {
                    wfFields.put(code, fieldValue.toString());
                }
            }
        }
        wfParamBuss.setBussId(bussId);
        wfParamBuss.setWfFields(wfFields);
        return wfParamBuss;
    }
}

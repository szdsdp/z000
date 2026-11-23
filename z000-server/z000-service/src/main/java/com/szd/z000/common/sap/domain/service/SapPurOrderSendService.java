package com.szd.z000.common.sap.domain.service;

import com.alibaba.fastjson.JSONObject;
import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientDestination;
import com.szd.z000.base.orm.mapper.mm.purOrderSend.PurOrderSendMapper;
import com.szd.z000.common.sap.domain.purOrderSend.PurOrderSendAdd;
import com.szd.z000.common.sap.domain.purOrderSend.PurOrderSendAddData;
import com.szd.z000.common.sap.domain.purOrderSend.PurOrderSendRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * SAP采购订单推送接口
 */
@Service
public class SapPurOrderSendService {
    private static final Logger logger = LoggerFactory.getLogger(SapPurOrderSendService.class);
    @Resource
    private PurOrderSendMapper purOrderSendMapper;
    @Resource
    private ClientCore clientCore;


    /***
     * 保存
     */
    public String save(ClientDestination destination, JSONObject sourceBody) {
        PurOrderSendRet purOrderSendRet = new PurOrderSendRet();
        // 1. 参数校验
        if (sourceBody == null) {
            purOrderSendRet.setZstau("E");
            purOrderSendRet.setZmess("请求体不能为空");
            destination.setStatus("E");
            destination.setMessText("请求体不能为空");
            return JSONObject.toJSONString(purOrderSendRet);
        }
        // 转换参数
        PurOrderSendAdd purOrderSendAdd = JSONObject.parseObject(String.valueOf(sourceBody), PurOrderSendAdd.class);
        if (purOrderSendAdd == null) {
            purOrderSendRet.setZstau("E");
            purOrderSendRet.setZmess("请求体转换失败");
            destination.setStatus("E");
            destination.setMessText("请求体转换失败");
            return JSONObject.toJSONString(purOrderSendRet);
        }

        // 数据类型
        String dtype = purOrderSendAdd.getDtype();
        List<PurOrderSendAddData> itemList = purOrderSendAdd.getItems();
        if (!CollectionUtils.isEmpty(itemList)) {

//            purOrderSendMapper.insert();
        }

        // 返回成功
        purOrderSendRet.setZstau("S");
        purOrderSendRet.setZmess("保存成功");
        destination.setStatus("S");
        destination.setMessText("保存成功");
        return JSONObject.toJSONString(purOrderSendRet);
    }
}

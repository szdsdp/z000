package com.szd.z000.web.sap.mm.orderSearch.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.szd.core.client.domain.common.ClientPageTableRet;
import com.szd.core.client.domain.common.ClientRangeQuery;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.service.ClientBaseController;
import com.szd.z000.web.sap.mm.orderSearch.service.OrderSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 采购订单抽取接口
 */
@Slf4j
@RestController
@RequestMapping("/z000/mm/orderSearch")
public class OrderSearchController extends ClientBaseController {
    @Resource
    private OrderSearchService orderSearchService;

    /**
     * 采购订单抽取查询
     */
    @PostMapping("/list")
    public ClientPageTableRet list(@RequestBody ClientRpParam param) {
        startPage(param.getPageNum(), param.getPageSize());
        return orderSearchService.queryList(param);
    }

    /**
     * SAP采购订单抽取查询-搜索帮助使用
     */
    @PostMapping("/listSap")
    public ClientPageTableRet listSap(@RequestBody JSONObject jsonObject) {
        String bukrs = null;
        String lifnr = null;
        String ebeln = null;
        String bsart = null;
        String ekorg = null;
        String ekgrp = null;
        Integer pageNum = 1;
        Integer pageSize = 20;

        // 获取 _paramGroup 数组
        JSONArray paramGroup = jsonObject.getJSONArray("_paramGroup");
        if (paramGroup != null) {
            for (int i = 0; i < paramGroup.size(); i++) {
                JSONObject param = paramGroup.getJSONObject(i);
                String name = param.getString("paramName");
                String value = param.getString("paramValue");

                if ("bukrs".equals(name)) {
                    bukrs = value;
                } else if ("lifnr".equals(name)) {
                    lifnr = value;
                } else if ("ebeln".equals(name)) {
                    ebeln = value;
                } else if ("bsart".equals(name)) {
                    bsart = value;
                } else if ("ekorg".equals(name)) {
                    ekorg = value;
                } else if ("ekgrp".equals(name)) {
                    ekgrp = value;
                }
            }
        }

        // 获取分页参数
        if (jsonObject.containsKey("pageNum")) {
            pageNum = jsonObject.getInteger("pageNum");
        }
        if (jsonObject.containsKey("pageSize")) {
            pageSize = jsonObject.getInteger("pageSize");
        }

        // 公司或供应商为空直接返回空结果
        if (bukrs == null || bukrs.isEmpty() || lifnr == null || lifnr.isEmpty()) {
            ClientPageTableRet result = new ClientPageTableRet();
            result.setCode(200);
            result.setMsg("查询成功");
            result.setRows(new ArrayList<>());
            result.setTotal(0);
            return result;
        }

        // 封装为ClientRpParam
        ClientRpParam param = new ClientRpParam();
        Map<String, List<ClientRangeQuery>> ranges = new HashMap<>();
        if (bukrs != null && !bukrs.isEmpty()) {
            ranges.put("bukrs", buildRange(bukrs));
        }
        if (lifnr != null && !lifnr.isEmpty()) {
            ranges.put("lifnr", buildRange(lifnr));
        }
        if (ebeln != null && !ebeln.isEmpty()) {
            ranges.put("ebeln", buildRange(ebeln));
        }
        if (bsart != null && !bsart.isEmpty()) {
            ranges.put("bsart", buildRange(bsart));
        }
        if (ekorg != null && !ekorg.isEmpty()) {
            ranges.put("ekorg", buildRange(ekorg));
        }
        if (ekgrp != null && !ekgrp.isEmpty()) {
            ranges.put("ekgrp", buildRange(ekgrp));
        }
        param.setRanges(ranges);
        param.setPageNum(pageNum);
        param.setPageSize(pageSize);

        startPage(param.getPageNum(), param.getPageSize());
        return orderSearchService.queryList(param);
    }

    private List<ClientRangeQuery> buildRange(String value) {
        ClientRangeQuery query = new ClientRangeQuery();
        query.setLow(value);
        List<ClientRangeQuery> list = new ArrayList<>();
        list.add(query);
        return list;
    }
}

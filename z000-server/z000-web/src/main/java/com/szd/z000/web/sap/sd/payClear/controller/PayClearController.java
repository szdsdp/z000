package com.szd.z000.web.sap.sd.payClear.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.szd.core.client.domain.common.ClientPageTableRet;
import com.szd.core.client.domain.common.ClientRangeQuery;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.service.ClientBaseController;
import com.szd.z000.web.sap.sd.payClear.service.PayClearService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客商未清项抽取接口
 */
@Slf4j
@RestController
@RequestMapping("/z000/sd/payClear")
public class PayClearController extends ClientBaseController {
    @Resource
    private PayClearService payClearService;

    /**
     * 客商未清项抽取查询
     */
    @PostMapping("/list")
    public ClientPageTableRet list(@RequestBody ClientRpParam param) {
        startPage(param.getPageNum(), param.getPageSize());
        return payClearService.queryList(param);
    }

    /**
     * SAP客商未清项抽取查询-搜索帮助使用
     */
    @PostMapping("/listSap")
    public ClientPageTableRet listSap(@RequestBody JSONObject jsonObject) {
        String bukrs = null;
        String lifnr = null;
        String kunnr = null;
        String blart = null;
        String hkont = null;
        String gjahr = null;
        String belnr = null;
        String bdate = null;
        String edate = null;
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
                } else if ("kunnr".equals(name)) {
                    kunnr = value;
                } else if ("blart".equals(name)) {
                    blart = value;
                } else if ("hkont".equals(name)) {
                    hkont = value;
                } else if ("gjahr".equals(name)) {
                    gjahr = value;
                } else if ("belnr".equals(name)) {
                    belnr = value;
                } else if ("bdateDateRange".equals(name)) {
                    bdate = value;
                } else if ("edateDateRange".equals(name)) {
                    edate = value;
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

        // 公司为空直接返回空结果
        if (bukrs == null || bukrs.isEmpty()) {
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
        if (kunnr != null && !kunnr.isEmpty()) {
            ranges.put("kunnr", buildRange(kunnr));
        }
        if (blart != null && !blart.isEmpty()) {
            ranges.put("blart", buildRange(blart));
        }
        if (hkont != null && !hkont.isEmpty()) {
            ranges.put("hkont", buildRange(hkont));
        }
        if (gjahr != null && !gjahr.isEmpty()) {
            ranges.put("gjahr", buildRange(gjahr));
        }
        if (belnr != null && !belnr.isEmpty()) {
            ranges.put("belnr", buildRange(belnr));
        }
        if (bdate != null && !bdate.isEmpty()) {
            ranges.put("budatStart", buildRange(bdate));
        }
        if (edate != null && !edate.isEmpty()) {
            ranges.put("budatEnd", buildRange(edate));
        }
        param.setRanges(ranges);
        param.setPageNum(pageNum);
        param.setPageSize(pageSize);

        startPage(param.getPageNum(), param.getPageSize());
        return payClearService.queryList(param);
    }

    private List<ClientRangeQuery> buildRange(String value) {
        ClientRangeQuery query = new ClientRangeQuery();
        query.setLow(value);
        List<ClientRangeQuery> list = new ArrayList<>();
        list.add(query);
        return list;
    }
}

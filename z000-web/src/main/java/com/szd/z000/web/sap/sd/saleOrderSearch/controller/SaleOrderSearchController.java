package com.szd.z000.web.sap.sd.saleOrderSearch.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.szd.core.client.domain.common.ClientPageTableRet;
import com.szd.core.client.domain.common.ClientRangeQuery;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.service.ClientBaseController;
import com.szd.z000.web.sap.sd.saleOrderSearch.service.SaleOrderSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 销售订单抽取接口
 */
@Slf4j
@RestController
@RequestMapping("/z000/sd/saleOrderSearch")
public class SaleOrderSearchController extends ClientBaseController {
    @Resource
    private SaleOrderSearchService saleOrderSearchService;

    /**
     * 销售订单抽取查询
     */
    @PostMapping("/list")
    public ClientPageTableRet list(@RequestBody ClientRpParam param) {
        startPage(param.getPageNum(), param.getPageSize());
        return saleOrderSearchService.queryList(param);
    }

    /**
     * SAP销售订单抽取查询-搜索帮助使用
     */
    @PostMapping("/listSap")
    public ClientPageTableRet listSap(@RequestBody JSONObject jsonObject) {
        String kunnr = null;
        String vbeln = null;
        String auart = null;
        String vkorg = null;
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

                if ("kunnr".equals(name)) {
                    kunnr = value;
                } else if ("vbeln".equals(name)) {
                    vbeln = value;
                } else if ("auart".equals(name)) {
                    auart = value;
                } else if ("vkorg".equals(name)) {
                    vkorg = value;
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

        // 客户为空直接返回空结果
        if (kunnr == null || kunnr.isEmpty()) {
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
        if (kunnr != null && !kunnr.isEmpty()) {
            ranges.put("kunnr", buildRange(kunnr));
        }
        if (vbeln != null && !vbeln.isEmpty()) {
            ranges.put("vbeln", buildRange(vbeln));
        }
        if (auart != null && !auart.isEmpty()) {
            ranges.put("auart", buildRange(auart));
        }
        if (vkorg != null && !vkorg.isEmpty()) {
            ranges.put("vkorg", buildRange(vkorg));
        }
        if (bdate != null && !bdate.isEmpty()) {
            ranges.put("bdate", buildRange(bdate));
        }
        if (edate != null && !edate.isEmpty()) {
            ranges.put("edate", buildRange(edate));
        }
        param.setRanges(ranges);
        param.setPageNum(pageNum);
        param.setPageSize(pageSize);

        startPage(param.getPageNum(), param.getPageSize());
        return saleOrderSearchService.queryList(param);
    }

    private List<ClientRangeQuery> buildRange(String value) {
        ClientRangeQuery query = new ClientRangeQuery();
        query.setLow(value);
        List<ClientRangeQuery> list = new ArrayList<>();
        list.add(query);
        return list;
    }
}

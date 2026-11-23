package com.szd.z000.web.sap.mm.mtlStock.controller;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientAjaxResult;
import com.szd.core.client.domain.common.ClientPageTableRet;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.service.ClientBaseController;
import com.szd.z000.base.orm.domain.mm.mtlStock.MtlStock;
import com.szd.z000.web.sap.mm.mtlStock.service.MtlStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 物料库存查询接口
 */
@Slf4j
@RestController
@RequestMapping("/z000/mm/mtlStock")
public class MtlStockController extends ClientBaseController {
    @Resource
    private MtlStockService mtlStockService;
    @Resource
    private ClientCore clientCore;

    /**
     * 物料库存查询（从数据库）
     */
    @PostMapping("/list")
    public ClientPageTableRet list(@RequestBody ClientRpParam param) {
        startPage(param.getPageNum(), param.getPageSize());
        List<MtlStock> list = mtlStockService.queryStockList(param);
        getDescList(list);
        return getPageTable(list);
    }

    /**
     * 同步库存（调用SAP并保存到数据库）
     */
    @PostMapping("/sync")
    public ClientAjaxResult sync(@RequestBody Map<String, String> param) {
        try {
            String matnr = param.get("matnr");
            String werks = param.get("werks");
            String lgort =  param.get("lgort");
            ClientResult result = mtlStockService.syncStock(matnr, werks, lgort);
            return ClientAjaxResult.success(result);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    private void getDescList(List<MtlStock> list) {
        if (!CollectionUtils.isEmpty(list)) {
            for (MtlStock stock : list) {
                stock.setWerksName(clientCore.mdm.getDescA("CORE_FACT", stock.getWerks(), "0"));
                stock.setLgortName(clientCore.mdm.getDescB("CORE_STOR", stock.getWerks(), stock.getLgort(), "0"));
                stock.setUnitName(clientCore.mdm.getDescA("CORE_UNIT", stock.getUnit(), "0"));
            }
        }
    }

    private String getRangeLow(ClientRpParam param, String field) {
        if (param.getRanges() != null && param.getRanges().get(field) != null
                && !param.getRanges().get(field).isEmpty()) {
            return param.getRanges().get(field).get(0).getLow();
        }
        return null;
    }
}

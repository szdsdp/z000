package com.szd.z000.web.sap.pp.orderExtract.controller;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.*;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.service.ClientBaseController;
import com.szd.z000.base.orm.domain.pp.orderExtract.OrderExtractHeader;
import com.szd.z000.web.sap.pp.orderExtract.domain.OrderExtractDataVO;
import com.szd.z000.web.sap.pp.orderExtract.service.OrderExtractService;
import com.szd.z000.web.sap.mdm.mtl.service.MtlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 生产订单抽取接口
 */
@Slf4j
@RestController
@RequestMapping("/z000/pp/orderExtract")
public class OrderExtractController extends ClientBaseController {
    @Resource
    private OrderExtractService orderExtractService;
    @Resource
    private MtlService mtlService;
    @Resource
    private ClientCore clientCore;

    /***
     * 列表
     */
    @PostMapping("/list")
    public ClientPageTableRet list(@RequestBody ClientRpParam param) {
        startPage(param.getPageNum(), param.getPageSize());
        List<OrderExtractHeader> list = orderExtractService.selectList(param);
        getDescList(list);
        return getPageTable(list);
    }

    /***
     * 查询详情
     */
    @GetMapping("/get")
    public ClientAjaxResult getData(@RequestParam String orderNo) {
        OrderExtractDataVO data = orderExtractService.selectData(orderNo);
        getDesc(data);
        return ClientAjaxResult.success(data);
    }

    /***
     * 抽取SAP生产订单
     */
    @PostMapping("/sync")
    public ClientAjaxResult sync(@RequestBody Map<String, String> param) {
        try {
            ClientResult clientResult = orderExtractService.sync(param);
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 填充列表的描述字段
     */
    private void getDescList(List<OrderExtractHeader> headerList) {
        if (!CollectionUtils.isEmpty(headerList)) {
            for (OrderExtractHeader header : headerList) {
                fillDesc(header);
            }
        }
    }

    /**
     * 填充详情描述字段
     */
    private void getDesc(OrderExtractDataVO data) {
        if (data.getHeader() != null) {
            fillDesc(data.getHeader());
        }
    }

    /**
     * 根据编码查询并填充名称
     */
    private void fillDesc(OrderExtractHeader header) {
        if (header.getMtlCode() != null && !header.getMtlCode().isEmpty()) {
            Map<String, String> nameMap = mtlService.getMtlNameMap(java.util.Collections.singletonList(header.getMtlCode()));
            header.setMtlName(nameMap.get(header.getMtlCode()));
        }
        if (header.getFact() != null && !header.getFact().isEmpty()) {
            header.setFactName(clientCore.mdm.getDescA("CORE_FACT", header.getFact(), "0"));
        }
    }
}

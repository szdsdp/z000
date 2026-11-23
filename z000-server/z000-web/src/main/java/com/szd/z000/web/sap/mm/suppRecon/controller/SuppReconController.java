package com.szd.z000.web.sap.mm.suppRecon.controller;

import com.szd.core.client.domain.common.ClientPageTableRet;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.service.ClientBaseController;
import com.szd.z000.web.sap.mm.suppRecon.service.SuppReconService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 供应商对账查询接口
 */
@Slf4j
@RestController
@RequestMapping("/z000/mm/suppRecon")
public class SuppReconController extends ClientBaseController {
    @Resource
    private SuppReconService suppReconService;

    /**
     * 供应商对账查询
     */
    @PostMapping("/list")
    public ClientPageTableRet list(@RequestBody ClientRpParam param) {
        startPage(param.getPageNum(), param.getPageSize());
        return suppReconService.queryList(param);
    }
}

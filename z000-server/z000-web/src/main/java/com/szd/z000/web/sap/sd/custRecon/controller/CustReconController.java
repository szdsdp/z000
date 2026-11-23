package com.szd.z000.web.sap.sd.custRecon.controller;

import com.szd.core.client.domain.common.ClientPageTableRet;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.service.ClientBaseController;
import com.szd.z000.web.sap.sd.custRecon.service.CustReconService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 客商对账查询接口
 */
@Slf4j
@RestController
@RequestMapping("/z000/sd/custRecon")
public class CustReconController extends ClientBaseController {
    @Resource
    private CustReconService custReconService;

    /**
     * 客商对账查询
     */
    @PostMapping("/list")
    public ClientPageTableRet list(@RequestBody ClientRpParam param) {
        startPage(param.getPageNum(), param.getPageSize());
        return custReconService.queryList(param);
    }
}

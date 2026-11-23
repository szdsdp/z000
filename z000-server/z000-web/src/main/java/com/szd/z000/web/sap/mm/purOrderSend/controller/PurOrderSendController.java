package com.szd.z000.web.sap.mm.purOrderSend.controller;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientPageTableRet;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.service.ClientBaseController;
import com.szd.z000.base.orm.domain.mm.purOrderSend.PurOrderSend;
import com.szd.z000.web.sap.mm.purOrderSend.service.PurOrderSendService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 采购订单推送接收
 */
@RestController
@RequestMapping("/z000/mm/purOrderSend")
public class PurOrderSendController extends ClientBaseController {
    @Resource
    private PurOrderSendService purOrderSendService;
    @Resource
    private ClientCore clientCore;

    @PostMapping("/list")
    public ClientPageTableRet list(@RequestBody ClientRpParam param) {
        startPage(param.getPageNum(), param.getPageSize());
        List<PurOrderSend> list = purOrderSendService.selectList(param);
        return getPageTable(list);
    }
}

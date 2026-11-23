package com.szd.z000.web.sap.mdm.mtl.controller;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.*;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.service.ClientBaseController;
import com.szd.z000.base.orm.domain.mdm.mtl.Mtl;
import com.szd.z000.web.sap.mdm.mtl.service.MtlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 物料主数据接口
 */
@Slf4j
@RestController
@RequestMapping("/z000/mdm/mtl")
public class MtlController extends ClientBaseController {
    @Resource
    private MtlService mtlService;
    @Resource
    private ClientCore clientCore;

    /**
     * 列表查询
     */
    @PostMapping("/list")
    public ClientPageTableRet list(@RequestBody ClientRpParam param) {
        startPage(param.getPageNum(), param.getPageSize());
        List<Mtl> list = mtlService.selectList(param);
        getDescList(list);
        return getPageTable(list);
    }

    private void getDescList(List<Mtl> list) {
        if (!CollectionUtils.isEmpty(list)) {
            for (Mtl mtl : list) {
                mtl.setUnitName(clientCore.mdm.getDescA("CORE_UNIT", mtl.getUnit(), "0"));
                mtl.setProdGroupName(clientCore.mdm.getDescA("Z000_MDM_PROD_GROUP", mtl.getProdGroup(), "0"));
            }
        }
    }

    /**
     * 同步SAP物料主数据
     */
    @PostMapping("/sync")
    public ClientAjaxResult sync(@RequestBody Map<String, String> param) {
        try {
            String bdate = param.get("bdate");
            String edate = param.get("edate");
            String matnr = param.get("matnr");
            String mtart = param.get("mtart");
            ClientResult clientResult = mtlService.sync(bdate, edate, matnr, mtart);
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }
}

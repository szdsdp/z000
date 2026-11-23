package com.szd.z000.web.demo.mdm.controller;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientAjaxResult;
import com.szd.core.client.domain.common.ClientPageTableRet;
import com.szd.core.client.service.ClientBaseController;
import com.szd.z000.base.orm.domain.demo.mdm.MdmCmpy;
import com.szd.z000.web.demo.mdm.service.MdmCmpyService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 公司代码接口层
 *
 * @author duanlizeng
 */
@RestController
@RequestMapping("/z000/demo/mdmCmpy")
public class MdmCmpyController extends ClientBaseController {
    @Resource
    private MdmCmpyService mdmCmpyService;
    @Resource
    private ClientCore clientCore;

    @PostMapping("/list")
    public ClientPageTableRet list(@RequestBody MdmCmpy mdmCmpy) {
        startPage();
        List<MdmCmpy> list = mdmCmpyService.selectList(mdmCmpy);
        list.forEach(com -> getDesc(com,"0"));
        return getPageTable(list);
    }

    @GetMapping(value = "/{cmpy}")
    public ClientAjaxResult getInfo(@PathVariable("cmpy") String cmpy) {
        MdmCmpy mdmCmpy = mdmCmpyService.selectById(cmpy);
        getDesc(mdmCmpy,"1");
        return ClientAjaxResult.success(mdmCmpy);
    }

    @PostMapping
    public ClientAjaxResult add(@RequestBody MdmCmpy mdmCmpy) {
        try {
            mdmCmpyService.insert(mdmCmpy);
        } catch (Exception e) {
            return ClientAjaxResult.error(e.getMessage());
        }
        return ClientAjaxResult.success();
    }

    @PutMapping
    public ClientAjaxResult edit(@RequestBody MdmCmpy mdmCmpy) {
        try {
            mdmCmpyService.update(mdmCmpy);
        } catch (Exception e) {
            return ClientAjaxResult.error(e.getMessage());
        }
        return ClientAjaxResult.success();
    }

    @DeleteMapping
    public ClientAjaxResult remove(@RequestBody String[] cmpys) {
        try {
            mdmCmpyService.deleteByIds(cmpys);
        } catch (Exception e) {
            return ClientAjaxResult.error(e.getMessage());
        }
        return ClientAjaxResult.success();
    }

    private void getDesc(MdmCmpy mdmCmpy,String flag) {
        mdmCmpy.setAccSetName(clientCore.mdm.getDescA("CORE_VCH_ACCS", mdmCmpy.getAccSet(), flag));
    }
}

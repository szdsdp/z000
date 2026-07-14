package com.szd.z000.web.z1.controller;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientAjaxResult;
import com.szd.core.client.domain.common.ClientPageTableRet;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.wf.ClientWfOper;
import com.szd.core.client.domain.wf.ClientWfReturn;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.service.ClientBaseController;
import com.szd.z000.base.orm.domain.z1.Z1Header;
import com.szd.z000.base.orm.domain.z1.Z1RpParam;
import com.szd.z000.web.z1.domain.Z1DataVO;
import com.szd.z000.web.z1.domain.Z1ParamVO;
import com.szd.z000.web.z1.domain.Z1ResultVO;
import com.szd.z000.web.z1.service.Z1HeaderService;
import com.szd.z000.web.z1.service.Z1ItemService;
import com.szd.z000.web.z1.service.Z1Service;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * z1功能相关接口
 */
@RestController
@RequestMapping("/z000/z1")
public class Z1Controller extends ClientBaseController {
    @Resource
    private Z1Service z1Service;
    @Resource
    private Z1HeaderService z1HeaderService;
    @Resource
    private Z1ItemService z1ItemService;
    @Resource
    private ClientCore clientCore;

    /**
     * 清单查询
     */
    @PostMapping("/list")
    public ClientPageTableRet list(@RequestBody Z1RpParam param) {
        startPage(param.getPageNum(), param.getPageSize());
        List<Z1Header> list = z1Service.selectList(param);
        z1HeaderService.getDescList(list);
        return getPageTable(list);
    }

    /**
     * 页面查询(包含业务数据、流程按钮、字段状态)
     */
    @GetMapping("/get")
    public ClientAjaxResult getData(String bussId, String userId, String option) {
        // 业务数据
        Z1DataVO data = z1Service.selectData(bussId);
        // 添加描述
        getDesc(data);
        // 流程操作授权
        ClientWfOper wfOper = clientCore.wf.wfInit(bussId, userId, option);
        // 设置工作流按钮状态
        setWfOper(data, wfOper);
        // 返回
        Z1ResultVO dataResultVO = new Z1ResultVO();
        dataResultVO.setData(data);
        dataResultVO.setWfOper(wfOper);
        return ClientAjaxResult.success(dataResultVO);
    }

    /**
     * 数据检查
     */
    @PostMapping("/check")
    public ClientAjaxResult check(@RequestBody Z1ParamVO paramVO) {
        try {
            ClientResult checkResult = z1Service.check(paramVO);
            return ClientAjaxResult.success(checkResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 页面按钮操作(保存草稿，提交，同意，驳回。。。)
     */
    @PostMapping("/save")
    public ClientAjaxResult save(@RequestBody Z1ParamVO z1ParamVO) {
        try {
            ClientWfReturn clientWfReturn = z1Service.saveData(z1ParamVO);
            return ClientAjaxResult.success(clientWfReturn);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 获取字段描述
     */
    private void getDesc(Z1DataVO data) {
        z1HeaderService.getDesc(data.getHeader());
        z1ItemService.getDesc(data.getItemList());
    }

    /***
     * 设置工作流按钮状态
     */
    private static void setWfOper(Z1DataVO data, ClientWfOper wfOper) {
        String bussStatus = data.getHeader().getBussStatus();
        if (bussStatus.equals("F") || bussStatus.equals("G")) {
            wfOper.getWfBtn().setBtn_delete("V");
            wfOper.getWfBtn().setBtn_revoke("V");
        }
    }
}

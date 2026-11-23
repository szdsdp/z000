package com.szd.z000.web.demo.expCost.controller;

import com.alibaba.fastjson.JSON;
import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientAjaxResult;
import com.szd.core.client.domain.common.ClientPageTableRet;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.mdm.ClientObjBase;
import com.szd.core.client.domain.tool.ClientAiSkillSqlParam;
import com.szd.core.client.domain.tool.ClientAiSkillSqlRet;
import com.szd.core.client.domain.wf.ClientWfOper;
import com.szd.core.client.domain.wf.ClientWfReturn;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.service.ClientBaseController;
import com.szd.core.client.service.ClientLocalCache;
import com.szd.z000.base.orm.domain.demo.expCost.ExpCostHeader;
import com.szd.z000.web.demo.expCost.domain.ExpCostDataVO;
import com.szd.z000.web.demo.expCost.domain.ExpCostParamVO;
import com.szd.z000.web.demo.expCost.domain.ExpCostResultVO;
import com.szd.z000.web.demo.expCost.service.ExpCostHeaderService;
import com.szd.z000.web.demo.expCost.service.ExpCostItemService;
import com.szd.z000.web.demo.expCost.service.ExpCostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 表单功能接口
 */
@RestController
@RequestMapping("/z000/demo/expCost")
public class ExpCostController extends ClientBaseController {
    private static final Logger logger = LoggerFactory.getLogger(ClientBaseController.class);
    @Resource
    private ExpCostService expCostService;
    @Resource
    private ExpCostHeaderService expCostHeaderService;
    @Resource
    private ExpCostItemService expCostItemService;
    @Resource
    private ClientCore clientCore;

    /**
     * 清单查询
     */
    @PostMapping("/list")
    public ClientPageTableRet list(@RequestBody ClientRpParam param) {
        String s1 = ClientLocalCache.getMdm("CORE_USER", "DUANLZ");
        String s2 = ClientLocalCache.getDict("CORE_DEV_ALIGN", "right");
        String s3 = clientCore.mdm.getDescA("CORE_DEV_ALIGN", "right", "0");
        ClientObjBase clientObjBase = ClientLocalCache.getMdm("CORE_BASE:DATA", "CORE_FSTG", ClientObjBase.class);
        ConcurrentHashMap<String, ConcurrentHashMap<String, Object>> all = ClientLocalCache.getMdmAll();
        ConcurrentHashMap<String, ConcurrentHashMap<String, Object>> allDict = ClientLocalCache.getDictAll();

        startPage(param.getPageNum(), param.getPageSize());
        List<ExpCostHeader> list = expCostService.selectList(param);
        expCostHeaderService.getDescList(list);
        return getPageTable(list);
    }

    /**
     * 页面查询(包含业务数据、流程按钮、字段状态)
     */
    @GetMapping("/get")
    public ClientAjaxResult getData(String bussId, String userId, String option) {
        // 业务数据
        ExpCostDataVO data = expCostService.selectData(bussId);
        // 添加描述
        getDesc(data);
        // 流程操作授权
        ClientWfOper wfOper = clientCore.wf.wfInit(bussId, userId, option);
        // 设置工作流按钮状态
        setWfOper(data, wfOper);
        // 返回
        ExpCostResultVO dataResultVO = new ExpCostResultVO();
        dataResultVO.setData(data);
        dataResultVO.setWfOper(wfOper);
        return ClientAjaxResult.success(dataResultVO);
    }

    /**
     * 数据检查
     */
    @PostMapping("/check")
    public ClientAjaxResult check(@RequestBody ExpCostParamVO expCostParamVO) {
        try {
            ClientResult checkResult = expCostService.check(expCostParamVO);
            return ClientAjaxResult.success(checkResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 页面按钮操作(保存草稿，提交，同意，驳回。。。)
     */
    @PostMapping("/save")
    public ClientAjaxResult saveData(@RequestBody ExpCostParamVO expCostParamVO) {
        try {
            ClientWfReturn clientWfReturn = expCostService.saveData(expCostParamVO);
            return ClientAjaxResult.success(clientWfReturn);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 获取字段描述
     */
    private void getDesc(ExpCostDataVO data) {
        expCostHeaderService.getDesc(data.getHeader());
        expCostItemService.getDesc(data.getItemList());
    }

    /***
     * 设置工作流按钮状态
     */
    private static void setWfOper(ExpCostDataVO data, ClientWfOper wfOper) {
        String bussStatus = data.getHeader().getBussStatus();
        if ("F".equals(bussStatus) || "G".equals(bussStatus)) {
            wfOper.getWfBtn().setBtn_delete("V");
            wfOper.getWfBtn().setBtn_revoke("V");
        }
    }

    @PostMapping("/test01")
    public ClientAjaxResult test01(@RequestBody ClientAiSkillSqlParam clientAiSkillSqlParam) {
        ClientAiSkillSqlRet clientAiSkillSqlRet = clientCore.tool.execSkillSql(clientAiSkillSqlParam);
        logger.info("clientAiSkillSqlRet:{}", JSON.toJSONString(clientAiSkillSqlRet));
        return ClientAjaxResult.success(clientAiSkillSqlRet);
    }
}

package com.szd.z000.web.sap.mm.check.checkTask.controller;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientAjaxResult;
import com.szd.core.client.domain.common.ClientPageTableRet;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.ClientWfOper;
import com.szd.core.client.domain.wf.ClientWfReturn;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.service.ClientBaseController;
import com.szd.z000.base.orm.domain.mm.check.checkPlan.CheckPlanHeader;
import com.szd.z000.base.orm.domain.mm.check.checkTask.CheckTaskHeader;
import com.szd.z000.base.orm.domain.mm.check.checkTask.CheckTaskItem;
import com.szd.z000.base.orm.mapper.mm.check.checkPlan.CheckPlanHeaderMapper;
import com.szd.z000.base.orm.mapper.mm.check.checkTask.CheckTaskItemMapper;
import com.szd.z000.web.sap.mm.check.checkTask.domain.CheckTaskDataVO;
import com.szd.z000.web.sap.mm.check.checkTask.domain.CheckTaskParamVO;
import com.szd.z000.web.sap.mm.check.checkTask.service.CheckTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 盘点任务接口
 */
@Slf4j
@RestController
@RequestMapping("/z000/mm/checkTask")
public class CheckTaskController extends ClientBaseController {
    @Resource
    private CheckTaskService checkTaskService;
    @Resource
    private CheckPlanHeaderMapper checkPlanHeaderMapper;
    @Resource
    private CheckTaskItemMapper checkTaskItemMapper;
    @Resource
    private ClientCore clientCore;

    /**
     * 列表查询
     */
    @PostMapping("/list")
    public ClientPageTableRet list(@RequestBody ClientRpParam param) {
        startPage(param.getPageNum(), param.getPageSize());
        List<CheckTaskHeader> list = checkTaskService.selectList(param);
        getDescList(list);
        return getPageTable(list);
    }

    /**
     * 页面查询
     */
    @GetMapping("/get")
    public ClientAjaxResult getData(String bussId, String userId, String option) {
        CheckTaskDataVO data = checkTaskService.selectData(bussId);
        getDesc(data);
        ClientWfOper wfOper = clientCore.wf.wfInit(bussId, userId, option);
        setWfOper(data, wfOper);
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("data", data);
        result.put("wfOper", wfOper);
        return ClientAjaxResult.success(result);
    }

    /**
     * 数据检查
     */
    @PostMapping("/check")
    public ClientAjaxResult check(@RequestBody CheckTaskParamVO paramVO) {
        try {
            return ClientAjaxResult.success(checkTaskService.check(paramVO));
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 更新行项目（扫码自动保存等场景，仅传变化行）
     */
    @PostMapping("/updateItems")
    public ClientAjaxResult updateItems(@RequestBody List<CheckTaskItem> items) {
        try {
            for (CheckTaskItem item : items) {
                checkTaskItemMapper.updateItem(item);
            }
            return ClientAjaxResult.success();
        } catch (Exception e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 保存/提交
     */
    @PostMapping("/save")
    public ClientAjaxResult saveData(@RequestBody CheckTaskParamVO paramVO) {
        try {
            ClientWfReturn clientWfReturn = checkTaskService.saveData(paramVO);
            return ClientAjaxResult.success(clientWfReturn);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    private void getDescList(List<CheckTaskHeader> headerList) {
        if (!CollectionUtils.isEmpty(headerList)) {
            for (CheckTaskHeader header : headerList) {
                header.setFactName(clientCore.mdm.getDescA("CORE_FACT", header.getFact(), "0"));
                header.setStoreName(clientCore.mdm.getDescB("CORE_STOR", header.getFact(), header.getStore(), "0"));
                header.setCreateByName(clientCore.mdm.getDescA("CORE_USER", header.getCreateBy(), "0"));
                header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "0"));
                header.setTaskStatusName(clientCore.mdm.getDescA("Z000_KCPD_TASK_STATUS", header.getTaskStatus(), "0"));
            }
        }
    }




    private void getDesc(CheckTaskDataVO data) {
        getDescHeader(data);
        getDescItem(data);
    }



    private void getDescHeader(CheckTaskDataVO data) {
        if (data.getHeader() != null) {
            CheckTaskHeader header = data.getHeader();
            header.setFactName(clientCore.mdm.getDescA("CORE_FACT", header.getFact(), "1"));
            header.setStoreName(clientCore.mdm.getDescB("CORE_STOR", header.getFact(), header.getStore(), "1"));
            header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "1"));
            header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "1"));
            header.setBstpName(clientCore.mdm.getDescA("CORE_BSTP", header.getBstp(), "1"));
            header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "1"));
            header.setCreateByName(clientCore.mdm.getDescA("CORE_USER", header.getCreateBy(), "1"));

            // 查询盘点计划单
            CheckPlanHeader checkPlanHeader = checkPlanHeaderMapper.selectById(header.getPlanId());
            if (checkPlanHeader != null) {
                header.setCheckPlanBussDocId(checkPlanHeader.getBussDocId());
            }
        }
    }


    private void getDescItem(CheckTaskDataVO data) {
        if (data.getItemList() != null) {
            for (CheckTaskItem item : data.getItemList()) {
                item.setCounterName(clientCore.mdm.getDescA("CORE_USER", item.getCounter(), "1"));
            }
        }
    }

    private void setWfOper(CheckTaskDataVO data, ClientWfOper wfOper) {
        String bussStatus = data.getHeader().getBussStatus();
        if ("D".equals(bussStatus)) {
            wfOper.getWfBtn().setBtn_delete("V");
            wfOper.getWfBtn().setBtn_revoke("V");
        }
    }
}

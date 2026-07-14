package com.szd.z000.web.sap.mm.check.checkPlan.controller;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.*;
import com.szd.core.client.domain.wf.ClientWfOper;
import com.szd.core.client.domain.wf.ClientWfReturn;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.security.ClientSecurity;
import com.szd.core.client.security.ClientUser;
import com.szd.core.client.service.ClientBaseController;
import com.szd.core.client.util.ClientUtilDate;
import com.szd.z000.base.orm.domain.mm.check.checkPlan.CheckPlanHeader;
import com.szd.z000.base.orm.domain.mm.check.checkPlan.CheckPlanItem;
import com.szd.z000.base.orm.domain.mm.check.checkTask.CheckTaskHeader;
import com.szd.z000.service.common.utils.Z000Util;
import com.szd.z000.web.sap.mm.check.checkPlan.domain.CheckPlanDataVO;
import com.szd.z000.web.sap.mm.check.checkPlan.domain.CheckPlanParamVO;
import com.szd.z000.web.sap.mm.check.checkPlan.domain.CheckPlanResultVO;
import com.szd.z000.web.sap.mm.check.checkPlan.domain.CheckPlanReverseParamVO;
import com.szd.z000.web.sap.mm.check.checkPlan.service.CheckPlanSapService;
import com.szd.z000.web.sap.mm.check.checkPlan.service.CheckPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 库存盘点接口
 */
@Slf4j
@RestController
@RequestMapping("/z000/mm/checkPlan")
public class CheckPlanController extends ClientBaseController {
    @Resource
    private CheckPlanService checkPlanService;
    @Resource
    private CheckPlanSapService checkPlanSapService;
    @Resource
    private ClientCore clientCore;

    /**
     * 列表查询
     */
    @PostMapping("/list")
    public ClientPageTableRet list(@RequestBody ClientRpParam param) {
        startPage(param.getPageNum(), param.getPageSize());
        List<CheckPlanHeader> list = checkPlanService.selectList(param);
        getDescList(list);
        return getPageTable(list);
    }

    /**
     * 页面查询(包含业务数据、流程按钮、字段状态)
     */
    @GetMapping("/get")
    public ClientAjaxResult getData(String bussId, String userId, String option) {
        CheckPlanDataVO data = checkPlanService.selectData(bussId);
        getDesc(data);
        ClientWfOper wfOper = clientCore.wf.wfInit(bussId, userId, option);
        setWfOper(data, wfOper);
        CheckPlanResultVO resultVO = new CheckPlanResultVO();
        resultVO.setData(data);
        resultVO.setWfOper(wfOper);
        return ClientAjaxResult.success(resultVO);
    }

    private void getDesc(CheckPlanDataVO data) {
        getDescHeader(data);
        getDescItemList(data);
    }

    private void getDescItemList(CheckPlanDataVO data) {
        if (!CollectionUtils.isEmpty(data.getItemList())) {
            for (CheckPlanItem item : data.getItemList()) {
                item.setFactName(clientCore.mdm.getDescA("CORE_FACT", item.getFact(), "1"));
                item.setStoreName(clientCore.mdm.getDescB("CORE_STOR", item.getFact(), item.getStore(), "1"));
            }
        }
    }

    /**
     * 数据检查
     */
    @PostMapping("/check")
    public ClientAjaxResult check(@RequestBody CheckPlanParamVO paramVO) {
        try {
            ClientResult checkResult = checkPlanService.check(paramVO);
            return ClientAjaxResult.success(checkResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 页面按钮操作(保存草稿，提交，同意，驳回...)
     */
    @PostMapping("/save")
    public ClientAjaxResult saveData(@RequestBody CheckPlanParamVO paramVO) {
        try {
            ClientWfReturn clientWfReturn = checkPlanService.saveData(paramVO);
            return ClientAjaxResult.success(clientWfReturn);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 同步SAP
     */
    @GetMapping("/syncSap/{planId}")
    public ClientAjaxResult genSapDoc(@PathVariable("planId") String planId) {
        try {
            ClientResult clientResult = checkPlanSapService.genSapCheckPlan(planId);
            if (CollectionUtils.isEmpty(clientResult.getRetMsgBox())) {
                ClientResultMsgBox clientResultMsgBox = new ClientResultMsgBox("2", "", "S", "无需同步");
                clientResult.getRetMsgBox().add(clientResultMsgBox);
            }
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 下发盘点任务
     */
    @PostMapping("/distribute")
    public ClientAjaxResult distribute(@RequestBody CheckPlanParamVO paramVO) {
        try {
            String planId = paramVO.getData().getHeader().getPlanId();
            List<CheckTaskHeader> taskList = checkPlanService.distribute(planId);
            return ClientAjaxResult.success(taskList);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 冲销库存盘点
     */
    @PostMapping("/reverse")
    public ClientAjaxResult reverse(@RequestBody CheckPlanReverseParamVO param) {
        try {
            ClientResult clientResult = checkPlanSapService.reverseSapCheckPlan(param.getPlanId(), param.getBudat());
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 从库存查询创建盘点单(生成A状态草稿)
     */
    @PostMapping("/createFromStock")
    public ClientAjaxResult createFromStock(@RequestBody CheckPlanParamVO paramVO) {
        try {
            CheckPlanDataVO data = paramVO.getData();
            CheckPlanHeader header = data.getHeader();
            // 当前登录用户
            ClientUser user = ClientSecurity.getLoginUser().getUser();
            // 生成单据ID
            String bussId = clientCore.tool.getNumSeq();
            header.setPlanId(bussId);
            header.setBussId(bussId);
            header.setCreateBy(ClientSecurity.getUserId());
            header.setCreateTime(new Date());
            header.setBussStatus("A");
            header.setCmpy(user.getCmpy());
            header.setCstc(user.getCstc());
            // 生成标题
            header.setTitle("库存盘点_" + ClientUtilDate.getDate("yyyy-MM-dd", new Date()));
            header.setBussDate(new Date());
            // 设置行项目号
            if (!CollectionUtils.isEmpty(data.getItemList())) {
                for (int i = 0; i < data.getItemList().size(); i++) {
                    data.getItemList().get(i).setItemNo(i + 1);
                }
            }
            Z000Util.saveBussBase(header);
            checkPlanService.insert(data);
            // 返回单据ID供前端跳转
            ClientResult clientResult = new ClientResult();
            clientResult.setStatus("S");
            clientResult.setDocId(bussId);
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    private void getDescList(List<CheckPlanHeader> headerList) {
        if (!CollectionUtils.isEmpty(headerList)) {
            for (CheckPlanHeader header : headerList) {
                header.setBussId(header.getPlanId());
                header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "0"));
                header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "0"));
                header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "0"));
                header.setCreateByName(clientCore.mdm.getDescA("CORE_USER", header.getCreateBy(), "0"));
                header.setUpdateByName(clientCore.mdm.getDescA("CORE_USER", header.getUpdateBy(), "0"));
            }
        }
    }

    private void getDescHeader(CheckPlanDataVO data) {
        if (data.getHeader() != null) {
            CheckPlanHeader header = data.getHeader();
            header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "1"));
            header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "1"));
            header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "1"));
        }
    }

    private static void setWfOper(CheckPlanDataVO data, ClientWfOper wfOper) {
        String bussStatus = data.getHeader().getBussStatus();
        if ("D".equals(bussStatus)) {
            wfOper.getWfBtn().setBtn_delete("V");
            wfOper.getWfBtn().setBtn_revoke("V");
        }
    }
}

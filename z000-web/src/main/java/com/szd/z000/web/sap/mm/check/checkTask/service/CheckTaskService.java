package com.szd.z000.web.sap.mm.check.checkTask.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.*;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.security.ClientSecurity;
import com.szd.core.client.util.ClientUtilString;
import com.szd.z000.base.orm.domain.mm.check.checkPlan.CheckPlanHeader;
import com.szd.z000.base.orm.domain.mm.check.checkTask.CheckTaskHeader;
import com.szd.z000.base.orm.domain.mm.check.checkTask.CheckTaskItem;
import com.szd.z000.base.orm.mapper.mm.check.checkPlan.CheckPlanHeaderMapper;
import com.szd.z000.base.orm.mapper.mm.check.checkTask.CheckTaskHeaderMapper;
import com.szd.z000.base.orm.mapper.mm.check.checkTask.CheckTaskItemMapper;
import com.szd.z000.service.common.utils.Z000Util;
import com.szd.z000.web.sap.mm.check.checkTask.domain.CheckTaskDataVO;
import com.szd.z000.web.sap.mm.check.checkTask.domain.CheckTaskParamVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 盘点任务-服务类
 */
@Service
public class CheckTaskService {
    private static final Logger logger = LoggerFactory.getLogger(CheckTaskService.class);

    @Resource
    private CheckTaskHeaderService checkTaskHeaderService;
    @Resource
    private CheckTaskItemService checkTaskItemService;
    @Resource
    private CheckPlanHeaderMapper checkPlanHeaderMapper;
    @Resource
    private ClientCore clientCore;
    @Resource
    private CheckTaskService checkTaskService;
    @Resource
    private CheckTaskHeaderMapper checkTaskHeaderMapper;
    @Resource
    private CheckTaskItemMapper checkTaskItemMapper;

    public List<CheckTaskHeader> selectList(ClientRpParam param) {
        return checkTaskHeaderService.selectList(param);
    }

    public CheckTaskDataVO selectData(String taskId) {
        CheckTaskDataVO dataVO = new CheckTaskDataVO();
        CheckTaskHeader header = checkTaskHeaderService.selectById(taskId);
        List<CheckTaskItem> itemList = checkTaskItemService.selectList(taskId);
        dataVO.setHeader(header);
        dataVO.setItemList(itemList);
        return dataVO;
    }

    public ClientResult check(CheckTaskParamVO paramVO) {
        ClientResult ret = new ClientResult();
        checkData(paramVO);
        ret.setStatus("S");
        ret.setMsg("检查通过");
        return ret;
    }

    private void checkData(CheckTaskParamVO paramVO) {
        CheckTaskDataVO data = paramVO.getData();
        CheckTaskHeader header = data.getHeader();
        ClientResult clientResult = clientCore.wf.wfCheckBtn(header.getTaskId(), ClientSecurity.getUserId(), "U", paramVO.getWfEvt().getOperate());
        if (clientResult != null && "E".equals(clientResult.getStatus())) {
            throw new ClientCustomException(clientResult.getMsg());
        }
    }

    public ClientWfReturn saveData(CheckTaskParamVO paramVO) {
        ClientWfReturn clientWfReturn = new ClientWfReturn();
        if ("btn_delete".equals(paramVO.getWfEvt().getOperate())) {
            paramVO.setData(selectData(paramVO.getData().getHeader().getTaskId()));
        } else {
            checkData(paramVO);
        }
        saveBussWfData(paramVO, clientWfReturn);
        // 审批通过后计算差异并更新计划状态
        if (!"E".equals(clientWfReturn.getRetCodeF()) && "D".equals(paramVO.getData().getHeader().getBussStatus())) {
            updateTaskDiffAndStatus(paramVO.getData());
            checkAndUpdatePlanStatus(paramVO.getData().getHeader().getPlanId());
        }
        return clientWfReturn;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveBussWfData(CheckTaskParamVO paramVO, ClientWfReturn clientWfReturn) {
        saveBussData(paramVO);
        ClientWfReturn wfReturn = approval(paramVO.getData(), paramVO.getWfEvt());
        ClientWfBussBase wfBussBase = wfReturn.getWfBussBase();
        BeanUtils.copyProperties(wfReturn, clientWfReturn);
        clientWfReturn.setBussId(paramVO.getData().getHeader().getTaskId());
        clientWfReturn.setBussDocId(paramVO.getData().getHeader().getBussDocId());
        if (!"E".equals(wfReturn.getRetCodeF())) {
            // 草稿状态的单据，作废时直接删除
            if ("btn_delete".equals(paramVO.getWfEvt().getOperate()) && "A".equals(paramVO.getData().getHeader().getBussStatus())) {
                deleteById(clientWfReturn.getBussId());
            } else {
                String oldStatus = paramVO.getData().getHeader().getBussStatus();
                // 工作流状态转换为业务状态
                String bussStatus = Z000Util.getBussStatus(wfBussBase.getStatus(), wfBussBase.getRejectStatus());
                paramVO.getData().getHeader().setBussStatus(bussStatus);
                paramVO.getData().getHeader().setBussDocId(wfReturn.getBussDocId());
                checkTaskHeaderService.updateStatusAndDocId(paramVO.getData().getHeader());
                // 撤回时回退状态
                if ("D".equals(oldStatus) && !"D".equals(bussStatus)) {
                    checkTaskHeaderMapper.updateTaskStatus(paramVO.getData().getHeader().getTaskId(), "01");
                    checkAndUpdatePlanStatus(paramVO.getData().getHeader().getPlanId());
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveBussData(CheckTaskParamVO paramVO) {
        try {
            CheckTaskDataVO data = paramVO.getData();
            CheckTaskHeader header = data.getHeader();
            if (StringUtils.isBlank(header.getTaskId())) {
                String taskId = clientCore.tool.getNumSeq();
                header.setTaskId(taskId);
                header.setBussId(taskId);
                header.setCreateBy(ClientSecurity.getUserId());
                header.setCreateTime(new Date());
                header.setBussStatus("A");
                Z000Util.saveBussBase(header);
                checkTaskHeaderService.insert(header);
                checkTaskItemService.save(taskId, data.getItemList());
            } else {
                header.setUpdateBy(ClientSecurity.getUserId());
                header.setUpdateTime(new Date());
                Z000Util.saveBussBase(header);
                checkTaskHeaderService.update(header);
                checkTaskItemService.save(header.getTaskId(), data.getItemList());
            }
        } catch (Exception e) {
            logger.error("保存失败", e);
            throw new ClientCustomException("保存失败:" + e.getMessage());
        }
    }

    public ClientWfReturn approval(CheckTaskDataVO data, ClientWfParamEvt wfEvt) {
        ClientWfReturn wfReturn = new ClientWfReturn();
        try {
            CheckTaskHeader header = data.getHeader();
            String bussId = header.getTaskId();
            ClientWfParamBuss wfParamBuss = Z000Util.setWfParamBuss(header, bussId);
            ClientWfParam wfParam = new ClientWfParam(wfEvt, wfParamBuss);
            wfReturn = clientCore.wf.wfCall(wfParam);
        } catch (Exception e) {
            wfReturn.setRetCodeF("E");
            wfReturn.setRetMsg(e.getMessage());
        }
        return wfReturn;
    }

    private void deleteById(String taskId) {
        checkTaskHeaderService.deleteById(taskId);
        checkTaskItemService.deleteByTaskId(taskId);
    }

    /**
     * 计算任务行项目差异并更新状态
     */
    private void updateTaskDiffAndStatus(CheckTaskDataVO data) {
        CheckTaskHeader taskHeader = data.getHeader();
        List<CheckTaskItem> taskItems = data.getItemList();
        if (CollectionUtils.isEmpty(taskItems)) return;
        for (CheckTaskItem item : taskItems) {
            BigDecimal sysQnt = item.getSysQnt() != null ? item.getSysQnt() : BigDecimal.ZERO;
            BigDecimal countQnt = item.getCountQnt() != null ? item.getCountQnt() : BigDecimal.ZERO;
            BigDecimal diffQnt = countQnt.subtract(sysQnt);
            item.setDiffQnt(diffQnt);
            int cmp = diffQnt.compareTo(BigDecimal.ZERO);
            item.setCountStatus(cmp == 0 ? "01" : cmp > 0 ? "02" : "03");
            item.setMoveType(cmp > 0 ? "701" : cmp < 0 ? "702" : null);
        }
        checkTaskItemService.save(taskHeader.getTaskId(), taskItems);
        checkTaskHeaderMapper.updateTaskStatus(taskHeader.getTaskId(), "02");
    }

    /**
     * 检查计划下所有任务是否都完成，更新 planStatus
     */
    private void checkAndUpdatePlanStatus(String planId) {
        List<CheckTaskHeader> allTasks = checkTaskHeaderMapper.selectByPlanId(planId);
        if (CollectionUtils.isEmpty(allTasks)) return;
        boolean allDone = true;
        for (CheckTaskHeader th : allTasks) {
            if (!"02".equals(th.getTaskStatus())) { allDone = false; break; }
        }
        checkPlanHeaderMapper.updatePlanStatus(planId, allDone ? "03" : "02");
    }
}

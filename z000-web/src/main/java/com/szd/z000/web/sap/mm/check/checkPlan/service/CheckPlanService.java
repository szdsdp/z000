package com.szd.z000.web.sap.mm.check.checkPlan.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.constants.ClientConstMdm;
import com.szd.core.client.domain.common.ClientMdmReqCommon;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.mdm.ClientObjBstp;
import com.szd.core.client.domain.wf.*;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.security.ClientSecurity;
import com.szd.core.client.security.ClientUser;
import com.szd.core.client.util.ClientUtilString;
import com.szd.z000.base.orm.domain.mm.check.checkPlan.CheckPlanHeader;
import com.szd.z000.base.orm.domain.mm.check.checkPlan.CheckPlanItem;
import com.szd.z000.base.orm.domain.mm.check.checkPlan.CheckPlanRelateVo;
import com.szd.z000.base.orm.domain.mm.check.checkTask.CheckTaskHeader;
import com.szd.z000.base.orm.domain.mm.check.checkTask.CheckTaskItem;
import com.szd.z000.base.orm.mapper.mm.check.checkTask.CheckTaskHeaderMapper;
import com.szd.z000.base.orm.mapper.mm.check.checkTask.CheckTaskItemMapper;
import com.szd.z000.common.sap.domain.mtlStock.SapMtlStockCtrRet;
import com.szd.z000.common.sap.domain.mtlStock.SapMtlStockCtrRetItem;
import com.szd.z000.common.sap.domain.service.SapMtlStockService;
import com.szd.z000.service.common.utils.Z000Util;
import com.szd.z000.web.sap.mm.check.checkTask.service.CheckTaskHeaderService;
import com.szd.z000.web.sap.mm.mtlStock.service.MtlStockService;
import com.szd.z000.web.sap.mm.check.checkPlan.domain.CheckPlanDataVO;
import com.szd.z000.web.sap.mm.check.checkPlan.domain.CheckPlanParamVO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 库存盘点-服务类
 */
@Service
public class CheckPlanService {
    private static final Logger logger = LoggerFactory.getLogger(CheckPlanService.class);

    @Resource
    private CheckPlanHeaderService checkPlanHeaderService;
    @Resource
    private CheckPlanItemService checkPlanItemService;
    @Resource
    private CheckPlanSapService checkPlanSapService;
    @Resource
    private ClientCore clientCore;
    @Resource
    private CheckPlanService checkPlanService;
    @Resource
    private SapMtlStockService sapMtlStockService;
    @Resource
    private CheckTaskHeaderMapper checkTaskHeaderMapper;
    @Resource
    private CheckTaskItemMapper checkTaskItemMapper;
    @Resource
    private CheckTaskHeaderService checkTaskHeaderService;
    @Resource
    private MtlStockService mtlStockService;

    public List<CheckPlanHeader> selectList(ClientRpParam param) {
        return checkPlanHeaderService.selectList(param);
    }

    public CheckPlanDataVO selectData(String planId) {
        CheckPlanDataVO dataVO = new CheckPlanDataVO();
        CheckPlanHeader header = checkPlanHeaderService.selectById(planId);
        List<CheckPlanItem> itemList = checkPlanItemService.selectList(planId);
        // 关联单据任务单
        List<CheckTaskHeader> taskList = checkTaskHeaderService.selectByPlanId(planId);
        // 将任务状态填充到计划行项目
        if (!CollectionUtils.isEmpty(itemList) && !CollectionUtils.isEmpty(taskList)) {
            for (CheckPlanItem item : itemList) {
                for (CheckTaskHeader task : taskList) {
                    if (item.getFact().equals(task.getFact()) && item.getStore().equals(task.getStore())) {
                        item.setTaskStatus(task.getTaskStatus());
                        break;
                    }
                }
            }
        }
        ArrayList<CheckPlanRelateVo> relateList = new ArrayList<>();
        // 关联单据
        for (CheckTaskHeader task : taskList) {
            CheckPlanRelateVo checkPlanRelateVo = new CheckPlanRelateVo();
            checkPlanRelateVo.setBussDocId(ClientUtilString.isNotEmpty(task.getBussDocId()) ? task.getBussDocId() : task.getTaskId());
            checkPlanRelateVo.setBussStatus(task.getBussStatus());
            checkPlanRelateVo.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", task.getBussStatus(), "0"));
            checkPlanRelateVo.setBussDocType("盘点任务单");
            checkPlanRelateVo.setBstp(task.getBstp());
            checkPlanRelateVo.setRouterCode(task.getRouterCode());
            checkPlanRelateVo.setBussId(task.getTaskId());
            checkPlanRelateVo.setTaskStatusName(clientCore.mdm.getDescA("Z000_KCPD_TASK_STATUS", task.getTaskStatus(), "0"));
            relateList.add(checkPlanRelateVo);
        }


        dataVO.setHeader(header);
        dataVO.setItemList(itemList);
        dataVO.setTaskList(taskList);
        dataVO.setRelateList(relateList);
        return dataVO;
    }

    public ClientResult check(CheckPlanParamVO paramVO) {
        ClientResult ret = new ClientResult();
        checkData(paramVO);
        ret.setStatus("S");
        ret.setMsg("检查通过");
        return ret;
    }

    private void checkData(CheckPlanParamVO paramVO) {
        CheckPlanDataVO data = paramVO.getData();
        CheckPlanHeader header = data.getHeader();
        ClientResult clientResult = clientCore.wf.wfCheckBtn(header.getPlanId(), ClientSecurity.getUserId(), "U", paramVO.getWfEvt().getOperate());
        if (clientResult != null && "E".equals(clientResult.getStatus())) {
            throw new ClientCustomException(clientResult.getMsg());
        }
        checkPlanHeaderService.checkData(header);
    }

    public ClientWfReturn saveData(CheckPlanParamVO paramVO) {
        ClientWfReturn clientWfReturn = new ClientWfReturn();
        if ("btn_delete".equals(paramVO.getWfEvt().getOperate())) {
            paramVO.setData(selectData(paramVO.getData().getHeader().getPlanId()));
        } else {
            checkData(paramVO);
        }
        saveBussWfData(paramVO, clientWfReturn);
        // 调用sap
        ClientResult clientResult = checkPlanSapService.genSapCheckPlan(paramVO.getData().getHeader().getPlanId());
        if (ClientUtilString.isNotBlank(clientResult.getStatus())) {
            if (CollectionUtils.isEmpty(clientWfReturn.getRetMsgBox())) {
                clientWfReturn.setRetMsgBox(new ArrayList<>());
            }
            clientWfReturn.getRetMsgBox().addAll(clientResult.getRetMsgBox());
        }
        return clientWfReturn;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveBussWfData(CheckPlanParamVO paramVO, ClientWfReturn clientWfReturn) {
        saveBussData(paramVO);
        ClientWfReturn wfReturn = approval(paramVO.getData(), paramVO.getWfEvt());
        ClientWfBussBase wfBussBase = wfReturn.getWfBussBase();
        BeanUtils.copyProperties(wfReturn, clientWfReturn);
        clientWfReturn.setBussId(paramVO.getData().getHeader().getPlanId());
        clientWfReturn.setBussDocId(paramVO.getData().getHeader().getBussDocId());
        if (!"E".equals(wfReturn.getRetCodeF())) {
            // 草稿状态的单据，作废时直接删除
            if ("btn_delete".equals(paramVO.getWfEvt().getOperate()) && "A".equals(paramVO.getData().getHeader().getBussStatus())) {
                deleteById(clientWfReturn.getBussId());
            } else {
                // 工作流状态转换为业务状态
                String bussStatus = Z000Util.getBussStatus(wfBussBase.getStatus(), wfBussBase.getRejectStatus());
                paramVO.getData().getHeader().setBussStatus(bussStatus);
                paramVO.getData().getHeader().setBussDocId(wfReturn.getBussDocId());
                checkPlanHeaderService.updateStatusAndDocId(paramVO.getData().getHeader());
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveBussData(CheckPlanParamVO paramVO) {
        try {
            CheckPlanDataVO data = paramVO.getData();
            CheckPlanHeader header = data.getHeader();
            if (StringUtils.isBlank(header.getPlanId())) {
                String bussId = clientCore.tool.getNumSeq();
                header.setPlanId(bussId);
                header.setBussId(bussId);
                header.setCreateBy(ClientSecurity.getUserId());
                header.setCreateTime(new Date());
                header.setBussStatus("A");
                Z000Util.saveBussBase(header);
                checkPlanService.insert(data);
            } else {
                header.setUpdateBy(ClientSecurity.getUserId());
                header.setUpdateTime(new Date());
                Z000Util.saveBussBase(header);
                checkPlanService.update(data, paramVO.getWfEvt().getOperate());
            }
        } catch (Exception e) {
            logger.error("保存失败", e);
            throw new ClientCustomException("保存失败:" + e.getMessage());
        }
    }

    public ClientWfReturn approval(CheckPlanDataVO data, ClientWfParamEvt wfEvt) {
        ClientWfReturn wfReturn = new ClientWfReturn();
        try {
            CheckPlanHeader header = data.getHeader();
            String bussId = header.getPlanId();
            ClientWfParamBuss wfParamBuss = Z000Util.setWfParamBuss(header, bussId);
            ClientWfParam wfParam = new ClientWfParam(wfEvt, wfParamBuss);
            wfReturn = clientCore.wf.wfCall(wfParam);
        } catch (Exception e) {
            wfReturn.setRetCodeF("E");
            wfReturn.setRetMsg(e.getMessage());
        }
        return wfReturn;
    }

    private void deleteById(String planId) {
        checkPlanHeaderService.deleteById(planId);
        checkPlanItemService.deleteByPlanId(planId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insert(CheckPlanDataVO data) {
        CheckPlanHeader header = data.getHeader();
        checkPlanHeaderService.insert(header);
        checkPlanItemService.save(header.getPlanId(), data.getItemList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(CheckPlanDataVO data, String operate) {
        String planId = data.getHeader().getPlanId();
        CheckPlanHeader headerOld = checkPlanHeaderService.selectObjById(planId);
        checkPlanHeaderService.update(data.getHeader());
        // 查询历史行项目（在save覆盖前查询）
        List<CheckPlanItem> itemListOld = checkPlanItemService.selectList(planId);
        checkPlanItemService.save(planId, data.getItemList());
        saveLog(data, headerOld, itemListOld, operate);
    }

    private void saveLog(CheckPlanDataVO data, CheckPlanHeader headerOld, List<CheckPlanItem> itemListOld, String operate) {
        CheckPlanHeader header = data.getHeader();
        clientCore.tool.clogSaveA(header.getPlanId(), "z000_mm_check_plan_header", operate, header, headerOld);
        clientCore.tool.clogSaveB(header.getPlanId(), "itemNo", "z000_mm_check_plan_item", operate, data.getItemList(), itemListOld);
    }

    /**
     * 下发盘点任务：按(工厂,库位)拆分生成任务，查询物料级库存
     */
    @Transactional(rollbackFor = Exception.class)
    public List<CheckTaskHeader> distribute(String planId) {
        CheckPlanHeader planHeader = checkPlanHeaderService.selectObjById(planId);
        if (planHeader == null || !"01".equals(planHeader.getPlanStatus())) {
            throw new ClientCustomException("当前计划状态不允许下发");
        }
        List<CheckPlanItem> planItems = checkPlanItemService.selectList(planId);
        if (CollectionUtils.isEmpty(planItems)) {
            throw new ClientCustomException("计划行项目为空，无法下发");
        }
        List<CheckTaskHeader> taskList = new ArrayList<>();
        // 按(fact, store)分组
        Map<String, List<CheckPlanItem>> groupMap = new java.util.LinkedHashMap<>();
        for (CheckPlanItem item : planItems) {
            String key = item.getFact() + "||" + item.getStore();
            groupMap.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
        }
        ClientUser user = ClientSecurity.getLoginUser().getUser();
        for (Map.Entry<String, List<CheckPlanItem>> entry : groupMap.entrySet()) {
            String[] keys = entry.getKey().split("\\|\\|");
            String fact = keys[0], store = keys[1];
            // 查询库存：获取物料级明细
            ClientMdmReqCommon stockReq = mtlStockService.buildStockReq(fact, store);
            SapMtlStockCtrRet ret = sapMtlStockService.callSap(stockReq);
            if (ret == null || ret.getHeader() == null || !"S".equals(ret.getHeader().getZstau())
                    || CollectionUtils.isEmpty(ret.getItems())) {
                continue;
            }
            // 创建任务抬头
            String taskId = clientCore.tool.getNumSeq();
            // 业务类型
            String bstp = "Z000_MM_PDRW02";

            // 1、查询业务类型数据
            ClientObjBstp clientObjBstp = clientCore.mdm.getObjA(ClientConstMdm.CORE_BSTP, bstp, ClientObjBstp.class);
            if (ObjectUtils.isEmpty(clientObjBstp)) {
                throw new ClientCustomException("业务类型配置不存在");
            }

            String routerCode = clientObjBstp.getRouterCode();

            CheckTaskHeader taskHeader = new CheckTaskHeader();
            taskHeader.setTaskId(taskId);
            taskHeader.setBussId(taskId);
            taskHeader.setPlanId(planId);
            taskHeader.setFact(fact);
            taskHeader.setStore(store);
            taskHeader.setTaskStatus("01");
            taskHeader.setBlindFlag(planHeader.getBlindFlag());
            taskHeader.setBussStatus("A");
            taskHeader.setBstp(bstp);
            taskHeader.setRouterCode(routerCode);
            taskHeader.setCmpy(user.getCmpy());
            taskHeader.setCstc(user.getCstc());
            taskHeader.setBussDate(new Date());
            taskHeader.setTitle("盘点任务_" + fact + "_" + store);
            taskHeader.setCreateBy(ClientSecurity.getUserId());
            taskHeader.setCreateTime(new Date());
            // 创建任务行项目（过滤零库存物料）
            List<CheckTaskItem> taskItems = new ArrayList<>();
            int itemNo = 1;
            for (SapMtlStockCtrRetItem retItem : ret.getItems()) {
                BigDecimal labst = retItem.getLabst() != null ? new BigDecimal(retItem.getLabst()) : BigDecimal.ZERO;
                if (labst.compareTo(BigDecimal.ZERO) <= 0) continue;
                CheckTaskItem taskItem = new CheckTaskItem();
                taskItem.setTaskId(taskId);
                taskItem.setItemNo(itemNo++);
                taskItem.setMtlCode(retItem.getMatnr());
                taskItem.setMtlName(retItem.getMaktx());
                taskItem.setUnit(retItem.getMeins());
                taskItem.setSysQnt(labst);
                taskItems.add(taskItem);
            }
            // 无有效库存则跳过该工厂库位
            if (taskItems.isEmpty()) continue;
            // 保存业务主体
            Z000Util.saveBussBase(taskHeader);
            checkTaskHeaderMapper.insert(taskHeader);
            checkTaskItemMapper.batchInsert(taskItems);
            taskList.add(taskHeader);
        }
        // 更新计划状态为盘点中
        checkPlanHeaderService.updatePlanStatus(planId, "02");
        return taskList;
    }

}

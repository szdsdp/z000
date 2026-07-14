package com.szd.z000.web.sap.mm.check.checkPlan.service;

import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientResultMsgBox;
import com.szd.core.client.util.ClientUtilDate;
import com.szd.z000.base.orm.domain.mm.check.checkPlan.CheckPlanHeader;
import com.szd.z000.base.orm.domain.mm.check.checkTask.CheckTaskHeader;
import com.szd.z000.base.orm.domain.mm.check.checkTask.CheckTaskItem;
import com.szd.z000.base.orm.mapper.mm.check.checkTask.CheckTaskHeaderMapper;
import com.szd.z000.base.orm.mapper.mm.check.checkTask.CheckTaskItemMapper;
import com.szd.z000.common.sap.domain.check.checkPlan.SapCheckPlanCtr;
import com.szd.z000.common.sap.domain.check.checkPlan.SapCheckPlanCtrHeader;
import com.szd.z000.common.sap.domain.check.checkPlan.SapCheckPlanCtrItem;
import com.szd.z000.common.sap.domain.check.checkPlan.SapCheckPlanReverseCtr;
import com.szd.z000.common.sap.domain.service.SapCheckPlanCreateService;
import com.szd.z000.common.sap.domain.service.SapCheckPlanReverseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存盘点-SAP服务
 */
@Service
public class CheckPlanSapService {
    private static final Logger logger = LoggerFactory.getLogger(CheckPlanSapService.class);

    @Resource
    private CheckPlanHeaderService checkPlanHeaderService;
    @Resource
    private CheckTaskHeaderMapper checkTaskHeaderMapper;
    @Resource
    private CheckTaskItemMapper checkTaskItemMapper;
    @Resource
    private SapCheckPlanCreateService sapCheckPlanCreateService;
    @Resource
    private SapCheckPlanReverseService sapCheckPlanReverseService;

    private static final String GM_CODE_CHECK_PLAN = "04";

    /**
     * 推送SAP创建盘点凭证
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClientResult genSapCheckPlan(String planId) {
        ClientResult clientResult = new ClientResult();
        CheckPlanHeader header = checkPlanHeaderService.selectObjById(planId);
        if (header == null) return clientResult;

        // 查询所有任务行项目，筛选差异行
        List<CheckTaskHeader> taskHeaders = checkTaskHeaderMapper.selectByPlanId(planId);
        List<CheckTaskItem> allDiffItems = new ArrayList<>();
        Map<String, CheckTaskHeader> taskHeaderMap = new HashMap<>();
        for (CheckTaskHeader th : taskHeaders) {
            taskHeaderMap.put(th.getTaskId(), th);
            List<CheckTaskItem> items = checkTaskItemMapper.selectList(th.getTaskId());
            if (!CollectionUtils.isEmpty(items)) {
                for (CheckTaskItem ti : items) {
                    if (ti.getCountStatus() != null && !"01".equals(ti.getCountStatus())) {
                        allDiffItems.add(ti);
                    }
                }
            }
        }
        if (CollectionUtils.isEmpty(allDiffItems)) {
            clientResult.setStatus("S");
            clientResult.setMsg("无差异项，无需同步SAP");
            return clientResult;
        }

        // 封装SAP参数-抬头
        SapCheckPlanCtrHeader sapHeader = new SapCheckPlanCtrHeader();
        sapHeader.setIfDocno(header.getBussDocId());
        sapHeader.setPstngDate(ClientUtilDate.dateToSap(header.getPostDate()));
        sapHeader.setDocDate(ClientUtilDate.dateToSap(header.getVchDate()));
        sapHeader.setHeaderTxt(header.getTitle());
        sapHeader.setGmCode(GM_CODE_CHECK_PLAN);

        // 封装SAP参数-行项目（从任务差异行组装）
        List<SapCheckPlanCtrItem> sapItems = new ArrayList<>();
        for (CheckTaskItem ti : allDiffItems) {
            SapCheckPlanCtrItem sapItem = new SapCheckPlanCtrItem();
            sapItem.setMaterial(ti.getMtlCode());
            CheckTaskHeader th = taskHeaderMap.get(ti.getTaskId());
            if (th != null) {
                sapItem.setPlant(th.getFact());
                sapItem.setStgeLoc(th.getStore());
            }
            sapItem.setMoveType(ti.getMoveType());
            if (ti.getDiffQnt() != null) {
                sapItem.setEntryQnt(ti.getDiffQnt().abs());
            }
            sapItem.setEntryUom(ti.getUnit());
            sapItem.setItemText(ti.getItemText());
            sapItems.add(sapItem);
        }

        SapCheckPlanCtr sapCheckPlanCtr = new SapCheckPlanCtr();
        sapCheckPlanCtr.setHeader(sapHeader);
        sapCheckPlanCtr.setItemList(sapItems);

        clientResult = sapCheckPlanCreateService.createCheckPlan(sapCheckPlanCtr, planId);
        return clientResult;
    }

    /**
     * 盘点-SAP冲销
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClientResult reverseSapCheckPlan(String planId, Date budat) {
        ClientResult clientResult = new ClientResult();
        CheckPlanHeader header = checkPlanHeaderService.selectObjById(planId);
        if (header == null || header.getMblnr() == null || header.getMblnr().isEmpty()) {
            clientResult.setStatus("E");
            clientResult.setMsg("未找到物料凭证，无法冲销");
            ClientResultMsgBox msgBox = new ClientResultMsgBox("E", "未找到物料凭证，无法冲销");
            clientResult.getRetMsgBox().add(msgBox);
            return clientResult;
        }
        SapCheckPlanReverseCtr sapCheckPlanReverseCtr = new SapCheckPlanReverseCtr();
        sapCheckPlanReverseCtr.setMatDoc(header.getMblnr());
        sapCheckPlanReverseCtr.setDocYear(header.getMjahr());
        sapCheckPlanReverseCtr.setBudat(ClientUtilDate.dateToSap(budat));
        clientResult = sapCheckPlanReverseService.reverseCheckPlan(sapCheckPlanReverseCtr, header.getPlanId());
        return clientResult;
    }
}

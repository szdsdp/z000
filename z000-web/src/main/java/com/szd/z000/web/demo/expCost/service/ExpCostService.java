package com.szd.z000.web.demo.expCost.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientResultMsgBox;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.*;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.security.ClientSecurity;
import com.szd.z000.base.orm.domain.demo.expCost.ExpCostHeader;
import com.szd.z000.base.orm.domain.demo.expCost.ExpCostItem;
import com.szd.z000.service.common.utils.Z000Util;
import com.szd.z000.web.demo.expCost.domain.ExpCostDataVO;
import com.szd.z000.web.demo.expCost.domain.ExpCostParamVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 业务逻辑
 */
@Service
public class ExpCostService {
    private static final Logger logger = LoggerFactory.getLogger(ExpCostService.class);
    @Resource
    private ExpCostHeaderService expCostHeaderService;
    @Resource
    private ExpCostItemService expCostItemService;
    @Resource
    private ClientCore clientCore;

    /**
     * 查询清单
     */
    public List<ExpCostHeader> selectList(ClientRpParam param) {
        return expCostHeaderService.selectList(param);
    }

    /**
     * 查询详情
     */
    public ExpCostDataVO selectData(String bussId) {
        ExpCostDataVO expCostDataVO = new ExpCostDataVO();
        // 抬头
        ExpCostHeader header = expCostHeaderService.selectById(bussId);
        // 行项目
        List<ExpCostItem> itemList = expCostItemService.selectList(bussId);
        expCostDataVO.setHeader(header);
        expCostDataVO.setItemList(itemList);
        return expCostDataVO;
    }

    /**
     * 保存数据
     */
    public ClientWfReturn saveData(ExpCostParamVO expCostParamVO) {
        ClientWfReturn clientWfReturn = new ClientWfReturn();
        ClientWfParamEvt wfEvt = expCostParamVO.getWfEvt();
        // 作废不校验
        if ("btn_delete".equals(wfEvt.getOperate())) {
            // 重新查询数据库数据
            expCostParamVO.setData(selectData(expCostParamVO.getData().getHeader().getBussId()));
        } else {
            // 校验
            ClientResult returnVO = checkResult(expCostParamVO);
            clientWfReturn.setRetMsgBox(returnVO.getRetMsgBox());
            clientWfReturn.setBussId(expCostParamVO.getData().getHeader().getBussId());
            if ("E".equals(returnVO.getStatus())) {
                clientWfReturn.setRetCodeF("E");
                clientWfReturn.setRetMsg("禁止操作");
                return clientWfReturn;
            } else if ("W".equals(returnVO.getStatus())) {
                if (!wfEvt.isWarningExec()) {
                    clientWfReturn.setRetCodeF("W");
                    clientWfReturn.setRetMsg("操作警告");
                    return clientWfReturn;
                }
            }
        }
        // 保存业务数据
        saveBussWfData(expCostParamVO, clientWfReturn);
        // 保存预算、关联申请、关联合同
        saveOther(clientWfReturn.getBussId());
        return clientWfReturn;
    }

    /**
     * 数据保存-业务数据及工作流
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveBussWfData(ExpCostParamVO expCostParamVO, ClientWfReturn clientWfReturn) {
        logger.info("时间1={}", System.currentTimeMillis());
        // 保存业务数据
        saveBussData(expCostParamVO);
        logger.info("时间2={}", System.currentTimeMillis());
        // 审批
        ClientWfReturn wfReturn = approval(expCostParamVO.getData(), expCostParamVO.getWfEvt());
        logger.info("时间3={}", System.currentTimeMillis());
        // 获取业务主体
        ClientWfBussBase wfBussBase = wfReturn.getWfBussBase();
        // 复制工作流返回消息
        BeanUtils.copyProperties(wfReturn, clientWfReturn);
        clientWfReturn.setBussId(expCostParamVO.getData().getHeader().getBussId());
        clientWfReturn.setBussDocId(expCostParamVO.getData().getHeader().getBussDocId());
        // 根据工作流状态更新业务单据
        if (!"E".equals(wfReturn.getRetCodeF())) {
            //草稿状态的单据，作废时直接删除
            if ("btn_delete".equals(expCostParamVO.getWfEvt().getOperate()) && "A".equals(expCostParamVO.getData().getHeader().getBussStatus())) {
                deleteById(clientWfReturn.getBussId());
            } else {
                // 工作流状态转换为业务状态
                String bussStatus = Z000Util.getBussStatus(wfBussBase.getStatus(), wfBussBase.getRejectStatus());
                expCostParamVO.getData().getHeader().setBussStatus(bussStatus);
                expCostParamVO.getData().getHeader().setBussDocId(wfReturn.getBussDocId());
                expCostHeaderService.updateStatusAndDocId(expCostParamVO.getData().getHeader());
            }
        }
        logger.info("时间4={}", System.currentTimeMillis());
    }

    /**
     * 保存草稿
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveBussData(ExpCostParamVO expCostParamVO) {
        try {
            // 业务数据
            ExpCostDataVO data = expCostParamVO.getData();
            ExpCostHeader header = data.getHeader();
            // 首次保存
            if (StringUtils.isBlank(header.getBussId())) {
                // 单据id
                String bussId = clientCore.tool.getNumSeq();
                header.setBussId(bussId);
                header.setCreateBy(ClientSecurity.getUserId());
                header.setCreateTime(new Date());
                header.setBussStatus("A");
                // 保存业务主体
                Z000Util.saveBussBase(header);
                // 新增
                insert(data);
            } else {
                header.setUpdateBy(ClientSecurity.getUserId());
                header.setUpdateTime(new Date());
                // 保存业务主体
                Z000Util.saveBussBase(header);
                // 更新
                update(data, expCostParamVO.getWfEvt().getOperate());
            }
        } catch (Exception e) {
            logger.error("保存失败", e);
            throw new ClientCustomException("保存失败:" + e.getMessage());
        }
    }

    /**
     * 流程审批
     */
    public ClientWfReturn approval(ExpCostDataVO data, ClientWfParamEvt wfEvt) {
        ClientWfReturn wfReturn = new ClientWfReturn();
        try {
            ExpCostHeader header = data.getHeader();
            String bussId = header.getBussId();
            // 设置流程字段
            ClientWfParamBuss wfParamBuss = Z000Util.setWfParamBuss(header, bussId);
            // 调用工作流
            ClientWfParam wfParam = new ClientWfParam(wfEvt, wfParamBuss);
            wfReturn = clientCore.wf.wfCall(wfParam);
        } catch (Exception e) {
            wfReturn.setRetCodeF("E");
            wfReturn.setRetMsg(e.getMessage());
        }
        return wfReturn;
    }

    /**
     * 数据保存-关联数据（如预算、申请记录、合同记录）
     */
    private void saveOther(String bussId) {
        ExpCostDataVO dataVO = selectData(bussId);
        // 保存关联数据

        // 模式业务异常，测试使用
//        clientCore.tool.saveBussLog(bussId, "审批", "模式业务异常，测试使用", "B");
    }

    /**
     * 业务数据新增
     */
    void insert(ExpCostDataVO data) {
        expCostHeaderService.insert(data.getHeader());
        expCostItemService.save(data.getHeader().getBussId(), data.getItemList());
    }

    /**
     * 业务数据更新
     */
    void update(ExpCostDataVO data, String operate) {
        String bussId = data.getHeader().getBussId();
        // 查询历史抬头
        ExpCostHeader headerOld = expCostHeaderService.selectById(bussId);
        List<ExpCostItem> itemListOld = expCostItemService.selectList(bussId);
        // 更新抬头
        expCostHeaderService.update(data.getHeader());
        expCostItemService.save(bussId, data.getItemList());
        // 保存修改日志
        saveLog(data, headerOld, itemListOld, operate);
    }

    /**
     * 记录日志
     */
    private void saveLog(ExpCostDataVO data, ExpCostHeader headerOld, List<ExpCostItem> itemListOld, String operate) {
        ExpCostHeader header = data.getHeader();
        clientCore.tool.clogSaveA(header.getBussId(), "z000_exp_cost_header", operate, header, headerOld);
        clientCore.tool.clogSaveB(header.getBussId(), "itemNo", "z000_exp_cost_item", operate, data.getItemList(), itemListOld);
    }

    void deleteById(String bussId) {
        expCostHeaderService.deleteById(bussId);
        expCostItemService.deleteById(bussId);
    }

    /**
     * 数据检查
     */
    public ClientResult check(ExpCostParamVO expCostParamVO) {
        return checkResult(expCostParamVO);
    }

    private ClientResult checkResult(ExpCostParamVO expCostParamVO) {
        ClientResult ret = new ClientResult();
        // 检查业务数据
        checkData(expCostParamVO);
        // 校验其他关联信息
        List<ClientResultMsgBox> retMsgBox1 = new ArrayList<>();
        ret.getRetMsgBox().addAll(retMsgBox1);
        boolean match = ret.getRetMsgBox().stream().anyMatch(t -> t.getRetCode().equals("E"));
        if (match) {
            ret.setStatus("E");
        } else {
            match = ret.getRetMsgBox().stream().anyMatch(t -> t.getRetCode().equals("W"));
            if (match) {
                ret.setStatus("W");
            } else {
                ret.setStatus("S");
                ret.setMsg("检查通过");
            }
        }
        return ret;
    }

    /**
     * 数据检查
     */
    private void checkData(ExpCostParamVO expCostParamVO) {
        expCostHeaderService.checkData(expCostParamVO.getData().getHeader());
        expCostItemService.checkData(expCostParamVO.getData().getItemList());
    }
}

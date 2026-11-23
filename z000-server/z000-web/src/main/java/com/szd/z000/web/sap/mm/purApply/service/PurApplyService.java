package com.szd.z000.web.sap.mm.purApply.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.*;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.security.ClientSecurity;
import com.szd.core.client.util.ClientUtilString;
import com.szd.z000.base.orm.domain.mm.purOrder.PurOrderHeader;
import com.szd.z000.base.orm.domain.mm.purOrder.PurOrderItem;
import com.szd.z000.base.orm.domain.mm.purOrder.PurOrderSched;
import com.szd.z000.base.orm.domain.mm.purApply.PurApplyHeader;
import com.szd.z000.base.orm.domain.mm.purApply.PurApplyItem;
import com.szd.z000.base.orm.domain.mm.purApply.PurApplyRelateVo;
import com.szd.z000.service.common.utils.Z000Util;
import com.szd.z000.web.sap.mm.purOrder.service.PurOrderHeaderService;
import com.szd.z000.web.sap.mm.purOrder.service.PurOrderService;
import com.szd.z000.web.sap.mm.purApply.domain.PurApplyDataVO;
import com.szd.z000.web.sap.mm.purApply.domain.PurApplyParamVO;
import com.szd.z000.web.sap.mm.purOrder.domain.PurOrderDataVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 采购申请-服务类
 */
@Service
public class PurApplyService {
    private static final Logger logger = LoggerFactory.getLogger(PurApplyService.class);

    @Resource
    private PurApplyHeaderService purApplyHeaderService;
    @Resource
    private PurApplyItemService purApplyItemService;
    @Resource
    private PurApplySapService purApplySapService;
    @Resource
    private PurOrderService purOrderService;
    @Resource
    private PurOrderHeaderService purOrderHeaderService;
    @Resource
    private ClientCore clientCore;
    @Resource
    private PurApplyService purApplyService;

    public List<PurApplyHeader> selectList(ClientRpParam param) {
        return purApplyHeaderService.selectList(param);
    }

    public PurApplyDataVO selectData(String applyId) {
        PurApplyDataVO dataVO = new PurApplyDataVO();
        PurApplyHeader header = purApplyHeaderService.selectById(applyId);
        dataVO.setHeader(header);
        dataVO.setItemList(purApplyItemService.selectList(applyId));
        // 关联单据：查询该采购申请生成的采购订单
        ArrayList<PurApplyRelateVo> relateList = new ArrayList<>();
        PurOrderHeader purOrderHeader = purOrderHeaderService.selectByApplyId(applyId);
        if (purOrderHeader != null && purOrderHeader.getOrderId() != null) {
            PurApplyRelateVo relateVo = new PurApplyRelateVo();
            relateVo.setBussDocId(ClientUtilString.isNotEmpty(purOrderHeader.getBussDocId()) ? purOrderHeader.getBussDocId() : purOrderHeader.getOrderId());
            relateVo.setBussStatus(purOrderHeader.getBussStatus());
            relateVo.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", purOrderHeader.getBussStatus(), "0"));
            relateVo.setBussDocType("采购订单");
            relateVo.setBstp(purOrderHeader.getBstp());
            relateVo.setRouterCode(purOrderHeader.getRouterCode());
            relateVo.setBussId(purOrderHeader.getOrderId());
            relateList.add(relateVo);
        }
        dataVO.setRelateList(relateList);
        return dataVO;
    }

    public ClientResult check(PurApplyParamVO paramVO) {
        ClientResult ret = new ClientResult();
        checkData(paramVO);
        ret.setStatus("S");
        ret.setMsg("检查通过");
        return ret;
    }

    private void checkData(PurApplyParamVO paramVO) {
        PurApplyDataVO data = paramVO.getData();
        PurApplyHeader header = data.getHeader();
        ClientResult clientResult = clientCore.wf.wfCheckBtn(header.getApplyId(), ClientSecurity.getUserId(), "U", paramVO.getWfEvt().getOperate());
        if (clientResult != null && "E".equals(clientResult.getStatus())) {
            throw new ClientCustomException(clientResult.getMsg());
        }
        purApplyHeaderService.checkData(header);
    }

    public ClientWfReturn saveData(PurApplyParamVO paramVO) {
        ClientWfReturn clientWfReturn = new ClientWfReturn();
        if ("btn_delete".equals(paramVO.getWfEvt().getOperate())) {
            paramVO.setData(selectData(paramVO.getData().getHeader().getApplyId()));
        } else {
            checkData(paramVO);
        }
        saveBussWfData(paramVO, clientWfReturn);
        ClientResult clientResult = purApplySapService.genSapPurApply(paramVO.getData().getHeader().getApplyId());
        if (ClientUtilString.isNotBlank(clientResult.getStatus())) {
            if (CollectionUtils.isEmpty(clientWfReturn.getRetMsgBox())) {
                clientWfReturn.setRetMsgBox(new ArrayList<>());
            }
            clientWfReturn.getRetMsgBox().addAll(clientResult.getRetMsgBox());
        }
        return clientWfReturn;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveBussWfData(PurApplyParamVO paramVO, ClientWfReturn clientWfReturn) {
        saveBussData(paramVO);
        ClientWfReturn wfReturn = approval(paramVO.getData(), paramVO.getWfEvt());
        ClientWfBussBase wfBussBase = wfReturn.getWfBussBase();
        BeanUtils.copyProperties(wfReturn, clientWfReturn);
        clientWfReturn.setBussId(paramVO.getData().getHeader().getApplyId());
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
                purApplyHeaderService.updateStatusAndDocId(paramVO.getData().getHeader());
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveBussData(PurApplyParamVO paramVO) {
        try {
            PurApplyDataVO data = paramVO.getData();
            PurApplyHeader header = data.getHeader();
            if (StringUtils.isBlank(header.getApplyId())) {
                String bussId = clientCore.tool.getNumSeq();
                header.setApplyId(bussId);
                header.setBussId(bussId);
                header.setCreateBy(ClientSecurity.getUserId());
                header.setCreateTime(new Date());
                header.setBussStatus("A");
                Z000Util.saveBussBase(header);
                purApplyService.insert(data);
            } else {
                header.setUpdateBy(ClientSecurity.getUserId());
                header.setUpdateTime(new Date());
                Z000Util.saveBussBase(header);
                purApplyService.update(data, paramVO.getWfEvt().getOperate());
            }
        } catch (Exception e) {
            logger.error("保存失败", e);
            throw new ClientCustomException("保存失败:" + e.getMessage());
        }
    }

    public ClientWfReturn approval(PurApplyDataVO data, ClientWfParamEvt wfEvt) {
        ClientWfReturn wfReturn = new ClientWfReturn();
        try {
            PurApplyHeader header = data.getHeader();
            String bussId = header.getApplyId();
            ClientWfParamBuss wfParamBuss = Z000Util.setWfParamBuss(header, bussId);
            ClientWfParam wfParam = new ClientWfParam(wfEvt, wfParamBuss);
            wfReturn = clientCore.wf.wfCall(wfParam);
        } catch (Exception e) {
            wfReturn.setRetCodeF("E");
            wfReturn.setRetMsg(e.getMessage());
        }
        return wfReturn;
    }

    private void deleteById(String applyId) {
        purApplyHeaderService.deleteById(applyId);
        purApplyItemService.deleteByApplyId(applyId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insert(PurApplyDataVO data) {
        PurApplyHeader header = data.getHeader();
        purApplyHeaderService.insert(header);
        purApplyItemService.save(header.getApplyId(), data.getItemList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(PurApplyDataVO data, String operate) {
        String applyId = data.getHeader().getApplyId();
        PurApplyHeader headerOld = purApplyHeaderService.selectObjById(applyId);
        purApplyHeaderService.update(data.getHeader());
        // 查询历史行项目（在save覆盖前查询）
        List<PurApplyItem> itemListOld = purApplyItemService.selectList(applyId);
        purApplyItemService.save(applyId, data.getItemList());
        saveLog(data, headerOld, itemListOld, operate);
    }

    /**
     * 从采购申请生成采购订单(草稿状态)
     */
    @Transactional(rollbackFor = Exception.class)
    public String createPurOrder(String applyId) {
        // 查询采购申请数据
        PurApplyDataVO purApplyData = selectData(applyId);
        PurApplyHeader purApplyHeader = purApplyData.getHeader();
        List<PurApplyItem> purApplyItemList = purApplyData.getItemList();
        if (purApplyHeader == null || purApplyHeader.getApplyId() == null) {
            throw new ClientCustomException("采购申请不存在");
        }
        // 校验：通过采购申请ID判断是否已生成过采购订单，已生成且未作废则不允许重复创建
        PurOrderHeader existingHeader = purOrderHeaderService.selectByApplyId(purApplyHeader.getApplyId());
        if (existingHeader != null && !"E".equals(existingHeader.getBussStatus())) {
            String orderId = existingHeader.getBussDocId() != null ? existingHeader.getBussDocId() : existingHeader.getOrderId();
            throw new ClientCustomException("该采购申请已生成采购订单[" + orderId + "]，不可重复创建");
        }
        // 生成采购订单ID
        String purOrderId = clientCore.tool.getNumSeq();
        // 构建采购订单抬头
        PurOrderHeader purOrderHeader = new PurOrderHeader();
        purOrderHeader.setOrderId(purOrderId);
        purOrderHeader.setBussId(purOrderId);
        purOrderHeader.setCmpy(purApplyHeader.getCmpy());
        purOrderHeader.setCstc(purApplyHeader.getCstc());
        purOrderHeader.setBstp("Z000_MM_CGDD01"); // 采购订单业务类型
        purOrderHeader.setRouterCode("Z000_MM_CGDD_01A");
        purOrderHeader.setBussDate(new Date());
        purOrderHeader.setBussStatus("A");
        purOrderHeader.setCreateBy(ClientSecurity.getUserId());
        purOrderHeader.setCreateTime(new Date());
        purOrderHeader.setTitle("采购订单_" + purApplyHeader.getTitle());
        purOrderHeader.setApplyId(purApplyHeader.getApplyId());
        // 保存业务主体
        Z000Util.saveBussBase(purOrderHeader);
        // 构建采购订单行项目
        List<PurOrderItem> purOrderItemList = new ArrayList<>();
        List<PurOrderSched> purOrderSchedList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(purApplyItemList)) {
            for (int i = 0; i < purApplyItemList.size(); i++) {
                PurApplyItem purApplyItem = purApplyItemList.get(i);
                PurOrderItem purOrderItem = new PurOrderItem();
                purOrderItem.setOrderId(purOrderId);
                purOrderItem.setItemNo(i + 1);
                purOrderItem.setMtlCode(purApplyItem.getMtlCode());
                purOrderItem.setFact(purApplyItem.getFact());
                if (purApplyItem.getApplyNum() != null) {
                    purOrderItem.setPurNum(purApplyItem.getApplyNum());
                }
                purOrderItem.setUnit(purApplyItem.getUnit());
                purOrderItem.setApplyNo(purApplyHeader.getApplyNo());
                purOrderItem.setApplyNoItem(purApplyItem.getItemNo());
                purOrderItem.setDeliveryDate(purApplyItem.getDeliveryDate());
                purOrderItemList.add(purOrderItem);
                // 构建交货计划（取需求日期和申请数量）
                PurOrderSched sched = new PurOrderSched();
                sched.setOrderId(purOrderId);
                sched.setItemNo(i + 1);
                sched.setSchedLine(1);
                sched.setDeliveryDate(purApplyItem.getNeedDate());
                if (purApplyItem.getApplyNum() != null) {
                    sched.setQuantity(purApplyItem.getApplyNum());
                }
                purOrderSchedList.add(sched);
            }
        }
        // 组装数据
        PurOrderDataVO purOrderData = new PurOrderDataVO();
        purOrderData.setHeader(purOrderHeader);
        purOrderData.setItemList(purOrderItemList);
        purOrderData.setSchedList(purOrderSchedList);
        // 保存采购订单
        purOrderService.insert(purOrderData);
        return purOrderId;
    }

    private void saveLog(PurApplyDataVO data, PurApplyHeader headerOld, List<PurApplyItem> itemListOld, String operate) {
        PurApplyHeader header = data.getHeader();
        clientCore.tool.clogSaveA(header.getApplyId(), "z000_mm_pur_apply_header", operate, header, headerOld);
        clientCore.tool.clogSaveB(header.getApplyId(), "itemNo", "z000_mm_pur_apply_item", operate, data.getItemList(), itemListOld);
    }
}

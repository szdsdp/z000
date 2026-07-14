package com.szd.z000.web.sap.mm.purStore.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.*;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.security.ClientSecurity;
import com.szd.core.client.util.ClientUtilString;
import com.szd.z000.base.orm.domain.mm.invCheck.InvCheckHeader;
import com.szd.z000.base.orm.domain.mm.purOrder.PurOrderHeader;
import com.szd.z000.base.orm.domain.mm.purOrder.PurOrderItem;
import com.szd.z000.base.orm.domain.mm.purStore.PurStoreHeader;
import com.szd.z000.base.orm.domain.mm.purStore.PurStoreItem;
import com.szd.z000.base.orm.domain.mm.purStore.PurStoreRelateVo;
import com.szd.z000.base.orm.mapper.mm.invCheck.InvCheckHeaderMapper;
import com.szd.z000.service.common.utils.Z000Util;
import com.szd.z000.web.sap.mm.purOrder.service.PurOrderHeaderService;
import com.szd.z000.web.sap.mm.purOrder.service.PurOrderItemService;
import com.szd.z000.web.sap.mm.purStore.domain.PurStoreDataVO;
import com.szd.z000.web.sap.mm.purStore.domain.PurStoreParamVO;
import com.szd.core.client.util.ClientUtilDate;
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
 * 物料凭证-服务类
 */
@Service
public class PurStoreService {
    private static final Logger logger = LoggerFactory.getLogger(PurStoreService.class);

    @Resource
    private PurStoreHeaderService purStoreHeaderService;
    @Resource
    private PurStoreItemService purStoreItemService;
    @Resource
    private PurStoreSapService purStoreSapService;
    @Resource
    private PurOrderHeaderService purOrderHeaderService;
    @Resource
    private PurOrderItemService purOrderItemService;
    @Resource
    private InvCheckHeaderMapper invCheckHeaderMapper;
    @Resource
    private ClientCore clientCore;
    @Resource
    private PurStoreService purStoreService;

    public List<PurStoreHeader> selectList(ClientRpParam param) {
        return purStoreHeaderService.selectList(param);
    }

    public PurStoreDataVO selectData(String storeId) {
        PurStoreDataVO dataVO = new PurStoreDataVO();
        PurStoreHeader header = purStoreHeaderService.selectById(storeId);
        List<PurStoreItem> itemList = purStoreItemService.selectList(storeId);
        dataVO.setHeader(header);
        dataVO.setItemList(itemList);
        // 关联单据：查询该物料凭证产生的发票校验单
        ArrayList<PurStoreRelateVo> relateList = new ArrayList<>();
        if (ClientUtilString.isNotBlank(header.getBussDocId())) {
            List<InvCheckHeader> invCheckList = invCheckHeaderMapper.selectByStoreBussDocId(header.getBussDocId());
            if (!CollectionUtils.isEmpty(invCheckList)) {
                for (InvCheckHeader invHeader : invCheckList) {
                    PurStoreRelateVo relateVo = new PurStoreRelateVo();
                    relateVo.setBussDocId(ClientUtilString.isNotEmpty(invHeader.getBussDocId()) ? invHeader.getBussDocId() : invHeader.getInvId());
                    relateVo.setBussStatus(invHeader.getBussStatus());
                    relateVo.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", invHeader.getBussStatus(), "0"));
                    relateVo.setBussDocType("发票校验单");
                    relateVo.setBstp(invHeader.getBstp());
                    relateVo.setRouterCode(invHeader.getRouterCode());
                    relateVo.setBussId(invHeader.getInvId());
                    relateList.add(relateVo);
                }
            }
        }
        dataVO.setRelateList(relateList);
        return dataVO;
    }

    public ClientResult check(PurStoreParamVO paramVO) {
        ClientResult ret = new ClientResult();
        checkData(paramVO);
        ret.setStatus("S");
        ret.setMsg("检查通过");
        return ret;
    }

    /**
     * 业务数据校验
     */
    private void checkData(PurStoreParamVO paramVO) {
        PurStoreDataVO data = paramVO.getData();
        PurStoreHeader header = data.getHeader();
        // 工作流按钮权限检查
        ClientResult clientResult = clientCore.wf.wfCheckBtn(header.getStoreId(), ClientSecurity.getUserId(), "U", paramVO.getWfEvt().getOperate());
        if (clientResult != null && "E".equals(clientResult.getStatus())) {
            throw new ClientCustomException(clientResult.getMsg());
        }
        // 检查基本信息
        purStoreHeaderService.checkData(header);
    }

    /**
     * 数据保存
     */
    public ClientWfReturn saveData(PurStoreParamVO paramVO) {
        ClientWfReturn clientWfReturn = new ClientWfReturn();
        // 作废不校验
        if ("btn_delete".equals(paramVO.getWfEvt().getOperate())) {
            // 重新查询数据库数据
            paramVO.setData(selectData(paramVO.getData().getHeader().getStoreId()));
        } else {
            // 检查业务数据
            checkData(paramVO);
        }
        // 保存业务数据
        saveBussWfData(paramVO, clientWfReturn);
        // 推送SAP
        ClientResult clientResult = purStoreSapService.genSapPurStore(paramVO.getData().getHeader().getStoreId());
        if (ClientUtilString.isNotBlank(clientResult.getStatus())) {
            if (CollectionUtils.isEmpty(clientWfReturn.getRetMsgBox())) {
                clientWfReturn.setRetMsgBox(new ArrayList<>());
            }
            clientWfReturn.getRetMsgBox().addAll(clientResult.getRetMsgBox());
        }
        return clientWfReturn;
    }

    /**
     * 数据保存-业务数据及工作流
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveBussWfData(PurStoreParamVO paramVO, ClientWfReturn clientWfReturn) {
        // 保存业务数据
        saveBussData(paramVO);
        // 审批
        ClientWfReturn wfReturn = approval(paramVO.getData(), paramVO.getWfEvt());
        // 获取业务主体
        ClientWfBussBase wfBussBase = wfReturn.getWfBussBase();
        // 复制工作流返回消息
        BeanUtils.copyProperties(wfReturn, clientWfReturn);
        clientWfReturn.setBussId(paramVO.getData().getHeader().getStoreId());
        clientWfReturn.setBussDocId(paramVO.getData().getHeader().getBussDocId());
        // 根据工作流状态更新业务单据
        if (!"E".equals(wfReturn.getRetCodeF())) {
            // 草稿状态的单据，作废时直接删除
            if ("btn_delete".equals(paramVO.getWfEvt().getOperate()) && "A".equals(paramVO.getData().getHeader().getBussStatus())) {
                deleteById(clientWfReturn.getBussId());
            } else {
                // 工作流状态转换为业务状态
                String bussStatus = Z000Util.getBussStatus(wfBussBase.getStatus(), wfBussBase.getRejectStatus());
                paramVO.getData().getHeader().setBussStatus(bussStatus);
                paramVO.getData().getHeader().setBussDocId(wfReturn.getBussDocId());
                purStoreHeaderService.updateStatusAndDocId(paramVO.getData().getHeader());
            }
        }
    }

    /**
     * 保存草稿
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveBussData(PurStoreParamVO paramVO) {
        try {
            PurStoreDataVO data = paramVO.getData();
            PurStoreHeader header = data.getHeader();
            // 首次保存
            if (StringUtils.isBlank(header.getStoreId())) {
                // 单据id
                String bussId = clientCore.tool.getNumSeq();
                header.setStoreId(bussId);
                header.setBussId(bussId);
                header.setCreateBy(ClientSecurity.getUserId());
                header.setCreateTime(new Date());
                header.setBussStatus("A");
                // 保存业务主体
                Z000Util.saveBussBase(header);
                // 新增
                purStoreService.insert(data);
            } else {
                header.setUpdateBy(ClientSecurity.getUserId());
                header.setUpdateTime(new Date());
                // 保存业务主体
                Z000Util.saveBussBase(header);
                // 更新
                purStoreService.update(data, paramVO.getWfEvt().getOperate());
            }
        } catch (Exception e) {
            logger.error("保存失败", e);
            throw new ClientCustomException("保存失败:" + e.getMessage());
        }
    }

    /**
     * 流程审批
     */
    public ClientWfReturn approval(PurStoreDataVO data, ClientWfParamEvt wfEvt) {
        ClientWfReturn wfReturn = new ClientWfReturn();
        try {
            PurStoreHeader header = data.getHeader();
            String bussId = header.getStoreId();
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

    private void deleteById(String storeId) {
        purStoreHeaderService.deleteById(storeId);
        purStoreItemService.deleteByPurStoreId(storeId);
    }

    /**
     * 业务数据新增
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(PurStoreDataVO data) {
        PurStoreHeader header = data.getHeader();
        purStoreHeaderService.insert(header);
        purStoreItemService.save(header.getStoreId(), data.getItemList());
    }

    /**
     * 业务数据更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(PurStoreDataVO data, String operate) {
        String storeId = data.getHeader().getStoreId();
        // 查询历史抬头
        PurStoreHeader headerOld = purStoreHeaderService.selectObjById(storeId);
        // 更新抬头
        purStoreHeaderService.update(data.getHeader());
        // 查询历史行项目（在save覆盖前查询）
        List<PurStoreItem> itemListOld = purStoreItemService.selectList(storeId);
        // 更新行项目
        purStoreItemService.save(storeId, data.getItemList());
        // 保存修改日志
        saveLog(data, headerOld, itemListOld, operate);
    }

    /**
     * 从采购订单清单批量创建物料凭证(草稿状态)
     */
    @Transactional(rollbackFor = Exception.class)
    public String createFromPurOrderList(List<String> orderIds) {
        if (CollectionUtils.isEmpty(orderIds)) {
            throw new ClientCustomException("请选择采购订单");
        }
        // 查询所有选中的采购订单，校验同一供应商
        String supp = null;
        List<PurOrderItem> allPurOrderItems = new ArrayList<>();
        PurOrderHeader firstHeader = null;
        for (String orderId : orderIds) {
            PurOrderHeader cgddHeader = purOrderHeaderService.selectObjById(orderId);
            if (cgddHeader == null || cgddHeader.getOrderId() == null) {
                throw new ClientCustomException("采购订单[" + orderId + "]不存在");
            }
            if (firstHeader == null) {
                firstHeader = cgddHeader;
                supp = cgddHeader.getSupp();
            } else if (supp != null && !supp.equals(cgddHeader.getSupp())) {
                throw new ClientCustomException("所选采购订单供应商不一致，请选择同一供应商的采购订单");
            }
            List<PurOrderItem> items = purOrderItemService.selectList(orderId);
            if (!CollectionUtils.isEmpty(items)) {
                allPurOrderItems.addAll(items);
            }
        }
        if (CollectionUtils.isEmpty(allPurOrderItems)) {
            throw new ClientCustomException("所选采购订单无行项目数据");
        }
        // 生成物料凭证ID
        String storeId = clientCore.tool.getNumSeq();
        // 构建物料凭证抬头
        PurStoreHeader purStoreHeader = new PurStoreHeader();
        purStoreHeader.setStoreId(storeId);
        purStoreHeader.setBussId(storeId);
        purStoreHeader.setCmpy(firstHeader.getCmpy());
        purStoreHeader.setCstc(firstHeader.getCstc());
        purStoreHeader.setBstp("Z000_MM_CGRK01");
        purStoreHeader.setRouterCode("Z000_MM_CGRK_01A");
        purStoreHeader.setBussDate(new Date());
        purStoreHeader.setBussStatus("A");
        purStoreHeader.setCreateBy(ClientSecurity.getUserId());
        purStoreHeader.setCreateTime(new Date());
        purStoreHeader.setTitle("物料凭证_" + ClientUtilDate.getDate("yyyy-MM-dd", new Date()));
        Z000Util.saveBussBase(purStoreHeader);
        // 构建物料凭证行项目
        List<PurStoreItem> purStoreItemList = new ArrayList<>();
        for (int i = 0; i < allPurOrderItems.size(); i++) {
            PurOrderItem cgddItem = allPurOrderItems.get(i);
            // 找到对应的采购订单抬头获取凭证号
            PurOrderHeader parentHeader = purOrderHeaderService.selectObjById(cgddItem.getOrderId());
            PurStoreItem purStoreItem = new PurStoreItem();
            purStoreItem.setItemNo(i + 1);
            purStoreItem.setMtlCode(cgddItem.getMtlCode());
            purStoreItem.setFact(cgddItem.getFact());
            purStoreItem.setQuantity(cgddItem.getPurNum());
            purStoreItem.setUnit(cgddItem.getUnit());
            purStoreItem.setOrderNo(parentHeader != null ? parentHeader.getVchNo() : "");
            // 采购订单行号按SAP格式转换（×10，5位补零），统一物料凭证中的订单项目编号格式
            purStoreItem.setOrderItemNo(String.format("%05d", cgddItem.getItemNo() * 10));
            purStoreItem.setRefBussDocId(parentHeader != null ? parentHeader.getBussDocId() : "");
            purStoreItem.setRefBstp(parentHeader != null ? parentHeader.getBstp() : "");
            purStoreItem.setItemText("");
            purStoreItemList.add(purStoreItem);
        }
        // 校验：采购订单行项目是否已生成过物料凭证
//        List<String> duplicates = purStoreItemService.checkDuplicate(purStoreItemList);
//        if (!duplicates.isEmpty()) {
//            throw new ClientCustomException("以下采购订单行已生成过物料凭证，不可重复生成：" + String.join("、", duplicates));
//        }
        // 保存
        PurStoreDataVO purStoreData = new PurStoreDataVO();
        purStoreData.setHeader(purStoreHeader);
        purStoreData.setItemList(purStoreItemList);
        purStoreService.insert(purStoreData);
        return storeId;
    }

    private void saveLog(PurStoreDataVO data, PurStoreHeader headerOld, List<PurStoreItem> itemListOld, String operate) {
        PurStoreHeader header = data.getHeader();
        clientCore.tool.clogSaveA(header.getStoreId(), "z000_mm_pur_store_header", operate, header, headerOld);
        clientCore.tool.clogSaveB(header.getStoreId(), "itemNo", "z000_mm_pur_store_item", operate, data.getItemList(), itemListOld);
    }
}

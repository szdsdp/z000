package com.szd.z000.web.sap.mm.purOrder.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.*;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.security.ClientSecurity;
import com.szd.core.client.util.ClientUtilString;
import com.szd.z000.base.orm.domain.mm.purOrder.PurOrderHeader;
import com.szd.z000.base.orm.domain.mm.purOrder.PurOrderItem;
import com.szd.z000.base.orm.domain.mm.purOrder.PurOrderRelateVo;
import com.szd.z000.base.orm.domain.mm.purOrder.PurOrderSched;
import com.szd.z000.base.orm.domain.mm.purStore.PurStoreHeader;
import com.szd.z000.service.common.utils.Z000Util;
import com.szd.z000.web.sap.mm.purOrder.domain.PurOrderDataVO;
import com.szd.z000.web.sap.mm.purOrder.domain.PurOrderParamVO;
import com.szd.z000.web.sap.mm.purStore.service.PurStoreHeaderService;
import com.szd.z000.web.sap.mm.purStore.service.PurStoreItemService;
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
 * 采购订单-服务类
 */
@Service
public class PurOrderService {
    private static final Logger logger = LoggerFactory.getLogger(PurOrderService.class);

    @Resource
    private PurOrderHeaderService purOrderHeaderService;
    @Resource
    private PurOrderItemService purOrderItemService;
    @Resource
    private PurOrderSchedService purOrderSchedService;
    @Resource
    private PurOrderSapService purOrderSapService;
    @Resource
    private PurStoreItemService purStoreItemService;
    @Resource
    private PurStoreHeaderService purStoreHeaderService;
    @Resource
    private ClientCore clientCore;
    @Resource
    private PurOrderService purOrderService;

    public List<PurOrderHeader> selectList(ClientRpParam param) {
        return purOrderHeaderService.selectList(param);
    }

    public PurOrderDataVO selectData(String orderId) {
        PurOrderDataVO dataVO = new PurOrderDataVO();
        PurOrderHeader header = purOrderHeaderService.selectById(orderId);
        List<PurOrderItem> itemList = purOrderItemService.selectList(orderId);
        List<PurOrderSched> schedList = purOrderSchedService.selectList(orderId);
        dataVO.setHeader(header);
        dataVO.setItemList(itemList);
        dataVO.setSchedList(schedList);
        // 关联单据：通过前置单据查询该采购订单产生的物料凭证单
        ArrayList<PurOrderRelateVo> relateList = new ArrayList<>();
        if (ClientUtilString.isNotBlank(header.getBussDocId()) && ClientUtilString.isNotBlank(header.getBstp())) {
            List<String> storeIds = purStoreItemService.selectStoreIdsByRefDoc(header.getBussDocId(), header.getBstp());
            if (!CollectionUtils.isEmpty(storeIds)) {
                for (String storeId : storeIds) {
                    PurStoreHeader storeHeader = purStoreHeaderService.selectObjById(storeId);
                    if (storeHeader != null && storeHeader.getStoreId() != null) {
                        PurOrderRelateVo relateVo = new PurOrderRelateVo();
                        relateVo.setBussDocId(ClientUtilString.isNotEmpty(storeHeader.getBussDocId()) ? storeHeader.getBussDocId() : storeHeader.getStoreId());
                        relateVo.setBussStatus(storeHeader.getBussStatus());
                        relateVo.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", storeHeader.getBussStatus(), "0"));
                        relateVo.setBussDocType("物料凭证单");
                        relateVo.setBstp(storeHeader.getBstp());
                        relateVo.setRouterCode(storeHeader.getRouterCode());
                        relateVo.setBussId(storeHeader.getStoreId());
                        relateList.add(relateVo);
                    }
                }
            }
        }
        dataVO.setRelateList(relateList);
        return dataVO;
    }

    public ClientResult check(PurOrderParamVO paramVO) {
        ClientResult ret = new ClientResult();
        checkData(paramVO);
        ret.setStatus("S");
        ret.setMsg("检查通过");
        return ret;
    }

    /**
     * 业务数据校验
     */
    private void checkData(PurOrderParamVO paramVO) {
        PurOrderDataVO data = paramVO.getData();
        PurOrderHeader header = data.getHeader();
        // 工作流按钮权限检查
        ClientResult clientResult = clientCore.wf.wfCheckBtn(header.getOrderId(), ClientSecurity.getUserId(), "U", paramVO.getWfEvt().getOperate());
        if (clientResult != null && "E".equals(clientResult.getStatus())) {
            throw new ClientCustomException(clientResult.getMsg());
        }
        // 检查基本信息
        purOrderHeaderService.checkData(header);
    }

    /**
     * 数据保存
     */
    public ClientWfReturn saveData(PurOrderParamVO paramVO) {
        ClientWfReturn clientWfReturn = new ClientWfReturn();
        // 作废不校验
        if ("btn_delete".equals(paramVO.getWfEvt().getOperate())) {
            // 重新查询数据库数据
            paramVO.setData(selectData(paramVO.getData().getHeader().getOrderId()));
        } else {
            // 检查业务数据
            checkData(paramVO);
        }
        // 保存业务数据
        saveBussWfData(paramVO, clientWfReturn);
        // 推送SAP
        ClientResult clientResult = purOrderSapService.genSapPurOrder(paramVO.getData().getHeader().getOrderId());
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
    public void saveBussWfData(PurOrderParamVO paramVO, ClientWfReturn clientWfReturn) {
        // 保存业务数据
        saveBussData(paramVO);
        // 审批
        ClientWfReturn wfReturn = approval(paramVO.getData(), paramVO.getWfEvt());
        // 获取业务主体
        ClientWfBussBase wfBussBase = wfReturn.getWfBussBase();
        // 复制工作流返回消息
        BeanUtils.copyProperties(wfReturn, clientWfReturn);
        clientWfReturn.setBussId(paramVO.getData().getHeader().getOrderId());
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
                purOrderHeaderService.updateStatusAndDocId(paramVO.getData().getHeader());
            }
        }
    }

    /**
     * 保存草稿
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveBussData(PurOrderParamVO paramVO) {
        try {
            PurOrderDataVO data = paramVO.getData();
            PurOrderHeader header = data.getHeader();
            // 首次保存
            if (StringUtils.isBlank(header.getOrderId())) {
                // 单据id
                String bussId = clientCore.tool.getNumSeq();
                header.setOrderId(bussId);
                header.setBussId(bussId);
                header.setCreateBy(ClientSecurity.getUserId());
                header.setCreateTime(new Date());
                header.setBussStatus("A");
                // 保存业务主体
                Z000Util.saveBussBase(header);
                // 新增
                purOrderService.insert(data);
            } else {
                header.setUpdateBy(ClientSecurity.getUserId());
                header.setUpdateTime(new Date());
                // 保存业务主体
                Z000Util.saveBussBase(header);
                // 更新
                purOrderService.update(data, paramVO.getWfEvt().getOperate());
            }
        } catch (Exception e) {
            logger.error("保存失败", e);
            throw new ClientCustomException("保存失败:" + e.getMessage());
        }
    }

    /**
     * 流程审批
     */
    public ClientWfReturn approval(PurOrderDataVO data, ClientWfParamEvt wfEvt) {
        ClientWfReturn wfReturn = new ClientWfReturn();
        try {
            PurOrderHeader header = data.getHeader();
            String bussId = header.getOrderId();
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

    private void deleteById(String orderId) {
        purOrderHeaderService.deleteById(orderId);
        purOrderItemService.deleteByPurOrderId(orderId);
        purOrderSchedService.deleteByPurOrderId(orderId);
    }

    /**
     * 业务数据新增
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(PurOrderDataVO data) {
        PurOrderHeader header = data.getHeader();
        purOrderHeaderService.insert(header);
        purOrderItemService.save(header.getOrderId(), data.getItemList());
        purOrderSchedService.save(header.getOrderId(), data.getSchedList());
    }

    /**
     * 业务数据更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(PurOrderDataVO data, String operate) {
        String orderId = data.getHeader().getOrderId();
        // 查询历史抬头
        PurOrderHeader headerOld = purOrderHeaderService.selectObjById(orderId);
        // 更新抬头
        purOrderHeaderService.update(data.getHeader());
        // 查询历史行项目（在save覆盖前查询）
        List<PurOrderItem> itemListOld = purOrderItemService.selectList(orderId);
        // 查询历史交货计划
        List<PurOrderSched> schedListOld = purOrderSchedService.selectList(orderId);
        // 更新行项目
        purOrderItemService.save(orderId, data.getItemList());
        // 更新交货计划
        purOrderSchedService.save(orderId, data.getSchedList());
        // 保存修改日志
        saveLog(data, headerOld, itemListOld, schedListOld, operate);
    }

    private void saveLog(PurOrderDataVO data, PurOrderHeader headerOld, List<PurOrderItem> itemListOld, List<PurOrderSched> schedListOld, String operate) {
        PurOrderHeader header = data.getHeader();
        clientCore.tool.clogSaveA(header.getOrderId(), "z000_mm_pur_order_header", operate, header, headerOld);
        clientCore.tool.clogSaveB(header.getOrderId(), "itemNo", "z000_mm_pur_order_item", operate, data.getItemList(), itemListOld);
        clientCore.tool.clogSaveB(header.getOrderId(), "schedNo", "z000_mm_pur_order_sched", operate, data.getSchedList(), schedListOld);
    }
}

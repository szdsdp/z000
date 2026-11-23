package com.szd.z000.web.sap.sd.saleDelivery.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;

import java.math.BigDecimal;

import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.*;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.security.ClientSecurity;
import com.szd.core.client.util.ClientUtilDate;
import com.szd.core.client.util.ClientUtilString;
import com.szd.z000.base.orm.domain.sd.saleDelivery.SaleDeliveryHeader;
import com.szd.z000.base.orm.domain.sd.saleDelivery.SaleDeliveryItem;
import com.szd.z000.base.orm.domain.sd.saleDelivery.SaleDeliveryPost;
import com.szd.z000.service.common.utils.Z000Util;
import com.szd.z000.web.sap.sd.saleDelivery.domain.SaleDeliveryDataVO;
import com.szd.z000.web.sap.sd.saleDelivery.domain.SaleDeliveryParamVO;
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
 * 销售交货单-服务类
 */
@Service
public class SaleDeliveryService {
    private static final Logger logger = LoggerFactory.getLogger(SaleDeliveryService.class);

    @Resource
    private SaleDeliveryHeaderService saleDeliveryHeaderService;
    @Resource
    private SaleDeliveryItemService saleDeliveryItemService;
    @Resource
    private SaleDeliveryPostService saleDeliveryPostService;
    @Resource
    private SaleDeliverySapService saleDeliverySapService;
    @Resource
    private ClientCore clientCore;
    @Resource
    private SaleDeliveryService saleDeliveryService;

    /**
     * 列表查询
     */
    public List<SaleDeliveryHeader> selectList(ClientRpParam param) {
        return saleDeliveryHeaderService.selectList(param);
    }

    /**
     * 查询单据详情
     */
    public SaleDeliveryDataVO selectData(String deliveryId) {
        SaleDeliveryDataVO dataVO = new SaleDeliveryDataVO();
        SaleDeliveryHeader header = saleDeliveryHeaderService.selectById(deliveryId);
        List<SaleDeliveryItem> itemList = saleDeliveryItemService.selectList(deliveryId);
        SaleDeliveryPost saleDeliveryPost = saleDeliveryPostService.selectById(deliveryId);
        dataVO.setHeader(header);
        dataVO.setItemList(itemList);
        dataVO.setPost(saleDeliveryPost);
        return dataVO;
    }

    /**
     * 数据检查
     */
    public ClientResult check(SaleDeliveryParamVO paramVO) {
        ClientResult ret = new ClientResult();
        checkData(paramVO);
        ret.setStatus("S");
        ret.setMsg("检查通过");
        return ret;
    }

    /**
     * 业务数据及工作流按钮权限校验
     */
    private void checkData(SaleDeliveryParamVO paramVO) {
        SaleDeliveryDataVO data = paramVO.getData();
        SaleDeliveryHeader header = data.getHeader();
        ClientResult clientResult = clientCore.wf.wfCheckBtn(header.getDeliveryId(), ClientSecurity.getUserId(), "U", paramVO.getWfEvt().getOperate());
        if (clientResult != null && "E".equals(clientResult.getStatus())) {
            throw new ClientCustomException(clientResult.getMsg());
        }
        saleDeliveryHeaderService.checkData(header);
        // 校验交货数量不能超过未交货数量
        checkDlvQty(data);
    }

    /**
     * 校验交货数量是否超过剩余未交货数量
     */
    private void checkDlvQty(SaleDeliveryDataVO data) {
        if (!CollectionUtils.isEmpty(data.getItemList())) {
            for (SaleDeliveryItem item : data.getItemList()) {
                if (item.getDlvQty() != null && item.getOrderQty() != null
                        && item.getOrderNo() != null && item.getOrderItemNo() != null) {
                    BigDecimal alreadyDelivered = saleDeliveryItemService.sumDlvQty(
                            item.getOrderNo(), item.getOrderItemNo(), data.getHeader().getDeliveryId());
                    BigDecimal remaining = item.getOrderQty().subtract(alreadyDelivered);
                    if (item.getDlvQty().compareTo(remaining) > 0) {
                        throw new ClientCustomException("行项目[" + item.getItemNo()
                                + "]交货数量(" + item.getDlvQty() + ")超过剩余未交货数量(" + remaining + ")");
                    }
                }
            }
        }
    }

    /**
     * 数据保存
     */
    public ClientWfReturn saveData(SaleDeliveryParamVO paramVO) {
        ClientWfReturn clientWfReturn = new ClientWfReturn();
        if ("btn_delete".equals(paramVO.getWfEvt().getOperate())) {
            paramVO.setData(selectData(paramVO.getData().getHeader().getDeliveryId()));
        } else {
            checkData(paramVO);
        }
        saveBussWfData(paramVO, clientWfReturn);
        ClientResult clientResult = saleDeliverySapService.genSapSaleDelivery(paramVO.getData().getHeader().getDeliveryId());
        if (ClientUtilString.isNotBlank(clientResult.getStatus())) {
            if (CollectionUtils.isEmpty(clientWfReturn.getRetMsgBox())) {
                clientWfReturn.setRetMsgBox(new ArrayList<>());
            }
            clientWfReturn.getRetMsgBox().addAll(clientResult.getRetMsgBox());
        }
        return clientWfReturn;
    }

    /**
     * 保存业务数据及执行工作流审批
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveBussWfData(SaleDeliveryParamVO paramVO, ClientWfReturn clientWfReturn) {
        saveBussData(paramVO);
        ClientWfReturn wfReturn = approval(paramVO.getData(), paramVO.getWfEvt());
        ClientWfBussBase wfBussBase = wfReturn.getWfBussBase();
        BeanUtils.copyProperties(wfReturn, clientWfReturn);
        clientWfReturn.setBussId(paramVO.getData().getHeader().getDeliveryId());
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
                saleDeliveryHeaderService.updateStatusAndDocId(paramVO.getData().getHeader());
            }
        }
    }

    /**
     * 保存草稿
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveBussData(SaleDeliveryParamVO paramVO) {
        try {
            SaleDeliveryDataVO data = paramVO.getData();
            SaleDeliveryHeader header = data.getHeader();
            if (StringUtils.isBlank(header.getDeliveryId())) {
                String bussId = clientCore.tool.getNumSeq();
                header.setDeliveryId(bussId);
                header.setBussId(bussId);
                header.setCreateBy(ClientSecurity.getUserId());
                header.setCreateTime(new Date());
                header.setBussStatus("A");
                Z000Util.saveBussBase(header);
                saleDeliveryService.insert(data);
            } else {
                header.setUpdateBy(ClientSecurity.getUserId());
                header.setUpdateTime(new Date());
                Z000Util.saveBussBase(header);
                saleDeliveryService.update(data, paramVO.getWfEvt().getOperate());
            }
        } catch (Exception e) {
            logger.error("保存失败", e);
            throw new ClientCustomException("保存失败:" + e.getMessage());
        }
    }

    /**
     * 流程审批
     */
    public ClientWfReturn approval(SaleDeliveryDataVO data, ClientWfParamEvt wfEvt) {
        ClientWfReturn wfReturn = new ClientWfReturn();
        try {
            SaleDeliveryHeader header = data.getHeader();
            String bussId = header.getDeliveryId();
            ClientWfParamBuss wfParamBuss = Z000Util.setWfParamBuss(header, bussId);
            ClientWfParam wfParam = new ClientWfParam(wfEvt, wfParamBuss);
            wfReturn = clientCore.wf.wfCall(wfParam);
        } catch (Exception e) {
            wfReturn.setRetCodeF("E");
            wfReturn.setRetMsg(e.getMessage());
        }
        return wfReturn;
    }

    /**
     * 删除单据
     */
    private void deleteById(String deliveryId) {
        saleDeliveryHeaderService.deleteById(deliveryId);
        saleDeliveryItemService.deleteByDeliveryId(deliveryId);
    }

    /**
     * 业务数据新增
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(SaleDeliveryDataVO data) {
        SaleDeliveryHeader header = data.getHeader();
        saleDeliveryHeaderService.insert(header);
        saleDeliveryItemService.save(header.getDeliveryId(), data.getItemList());
    }

    /**
     * 业务数据更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(SaleDeliveryDataVO data, String operate) {
        String deliveryId = data.getHeader().getDeliveryId();
        SaleDeliveryHeader headerOld = saleDeliveryHeaderService.selectObjById(deliveryId);
        List<SaleDeliveryItem> itemListOld = saleDeliveryItemService.selectList(deliveryId);
        saleDeliveryHeaderService.update(data.getHeader());
        saleDeliveryItemService.save(deliveryId, data.getItemList());
        saveLog(data, headerOld, itemListOld, operate);
    }

    /**
     * 记录修改日志
     */
    private void saveLog(SaleDeliveryDataVO data, SaleDeliveryHeader headerOld, List<SaleDeliveryItem> itemListOld, String operate) {
        SaleDeliveryHeader header = data.getHeader();
        clientCore.tool.clogSaveA(header.getDeliveryId(), "z000_sd_sale_delivery_header", operate, header, headerOld);
        clientCore.tool.clogSaveB(header.getDeliveryId(), "itemNo", "z000_sd_sale_delivery_item", operate, data.getItemList(), itemListOld);
    }
}

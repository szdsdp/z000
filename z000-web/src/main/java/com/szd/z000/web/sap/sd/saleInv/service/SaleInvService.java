package com.szd.z000.web.sap.sd.saleInv.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.*;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.security.ClientSecurity;
import com.szd.core.client.util.ClientUtilString;
import com.szd.z000.base.orm.domain.sd.saleInv.SaleInvHeader;
import com.szd.z000.base.orm.domain.sd.saleInv.SaleInvItem;
import com.szd.z000.service.common.utils.Z000Util;
import com.szd.z000.web.sap.sd.saleInv.domain.SaleInvDataVO;
import com.szd.z000.web.sap.sd.saleInv.domain.SaleInvParamVO;
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
 * 销售开票-服务类
 */
@Service
public class SaleInvService {
    private static final Logger logger = LoggerFactory.getLogger(SaleInvService.class);

    @Resource
    private SaleInvHeaderService saleInvHeaderService;
    @Resource
    private SaleInvItemService saleInvItemService;
    @Resource
    private SaleInvSapService saleInvSapService;
    @Resource
    private ClientCore clientCore;
    @Resource
    private SaleInvService saleInvService;

    /**
     * 列表查询
     */
    public List<SaleInvHeader> selectList(ClientRpParam param) {
        return saleInvHeaderService.selectList(param);
    }

    /**
     * 查询单据详情
     */
    public SaleInvDataVO selectData(String invId) {
        SaleInvDataVO dataVO = new SaleInvDataVO();
        SaleInvHeader header = saleInvHeaderService.selectById(invId);
        List<SaleInvItem> itemList = saleInvItemService.selectList(invId);
        dataVO.setHeader(header);
        dataVO.setItemList(itemList);
        return dataVO;
    }

    /**
     * 数据检查
     */
    public ClientResult check(SaleInvParamVO paramVO) {
        ClientResult ret = new ClientResult();
        checkData(paramVO);
        ret.setStatus("S");
        ret.setMsg("检查通过");
        return ret;
    }

    /**
     * 业务数据及工作流按钮权限校验
     */
    private void checkData(SaleInvParamVO paramVO) {
        SaleInvDataVO data = paramVO.getData();
        SaleInvHeader header = data.getHeader();
        ClientResult clientResult = clientCore.wf.wfCheckBtn(header.getInvId(), ClientSecurity.getUserId(), "U", paramVO.getWfEvt().getOperate());
        if (clientResult != null && "E".equals(clientResult.getStatus())) {
            throw new ClientCustomException(clientResult.getMsg());
        }
        saleInvHeaderService.checkData(header);
        // 校验交货单号不能为空
        checkVbelnVl(data);
    }

    /**
     * 校验行项目交货单号不能为空且不能重复开票
     */
    private void checkVbelnVl(SaleInvDataVO data) {
        if (CollectionUtils.isEmpty(data.getItemList())) {
            throw new ClientCustomException("行项目不能为空");
        }
        List<String> vbelnVlList = new ArrayList<>();
        for (SaleInvItem item : data.getItemList()) {
            if (StringUtils.isBlank(item.getVbelnVl())) {
                throw new ClientCustomException("行项目[" + item.getItemNo() + "]交货单号不能为空");
            }
            vbelnVlList.add(item.getVbelnVl());
        }
        // 检查交货单号是否已开票（排除当前单据、已作废(状态E)、已冲销(有冲销发票号)的开票单）
        String excludeInvId = data.getHeader().getInvId();
        List<String> invoicedList = saleInvItemService.selectInvoicedVbelnVl(vbelnVlList, excludeInvId);
        if (!CollectionUtils.isEmpty(invoicedList)) {
            throw new ClientCustomException("交货单号[" + String.join(",", invoicedList) + "]已开票，不能重复开票");
        }
    }

    /**
     * 数据保存
     */
    public ClientWfReturn saveData(SaleInvParamVO paramVO) {
        ClientWfReturn clientWfReturn = new ClientWfReturn();
        if ("btn_delete".equals(paramVO.getWfEvt().getOperate())) {
            paramVO.setData(selectData(paramVO.getData().getHeader().getInvId()));
        } else {
            checkData(paramVO);
        }
        saveBussWfData(paramVO, clientWfReturn);
        ClientResult clientResult = saleInvSapService.issueInvoice(paramVO.getData().getHeader().getInvId());
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
    public void saveBussWfData(SaleInvParamVO paramVO, ClientWfReturn clientWfReturn) {
        saveBussData(paramVO);
        ClientWfReturn wfReturn = approval(paramVO.getData(), paramVO.getWfEvt());
        ClientWfBussBase wfBussBase = wfReturn.getWfBussBase();
        BeanUtils.copyProperties(wfReturn, clientWfReturn);
        clientWfReturn.setBussId(paramVO.getData().getHeader().getInvId());
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
                saleInvHeaderService.updateStatusAndDocId(paramVO.getData().getHeader());
            }
        }
    }

    /**
     * 保存草稿
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveBussData(SaleInvParamVO paramVO) {
        try {
            SaleInvDataVO data = paramVO.getData();
            SaleInvHeader header = data.getHeader();
            if (StringUtils.isBlank(header.getInvId())) {
                String bussId = clientCore.tool.getNumSeq();
                header.setInvId(bussId);
                header.setBussId(bussId);
                header.setCreateBy(ClientSecurity.getUserId());
                header.setCreateTime(new Date());
                header.setBussStatus("A");
                Z000Util.saveBussBase(header);
                saleInvService.insert(data);
            } else {
                header.setUpdateBy(ClientSecurity.getUserId());
                header.setUpdateTime(new Date());
                Z000Util.saveBussBase(header);
                saleInvService.update(data, paramVO.getWfEvt().getOperate());
            }
        } catch (Exception e) {
            logger.error("保存失败", e);
            throw new ClientCustomException("保存失败:" + e.getMessage());
        }
    }

    /**
     * 流程审批
     */
    public ClientWfReturn approval(SaleInvDataVO data, ClientWfParamEvt wfEvt) {
        ClientWfReturn wfReturn = new ClientWfReturn();
        try {
            SaleInvHeader header = data.getHeader();
            String bussId = header.getInvId();
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
    private void deleteById(String invId) {
        saleInvHeaderService.deleteById(invId);
        saleInvItemService.save(invId, null);
    }

    /**
     * 业务数据新增
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(SaleInvDataVO data) {
        SaleInvHeader header = data.getHeader();
        saleInvHeaderService.insert(header);
        saleInvItemService.save(header.getInvId(), data.getItemList());
    }

    /**
     * 业务数据更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(SaleInvDataVO data, String operate) {
        String invId = data.getHeader().getInvId();
        SaleInvHeader headerOld = saleInvHeaderService.selectObjById(invId);
        List<SaleInvItem> itemListOld = saleInvItemService.selectList(invId);
        saleInvHeaderService.update(data.getHeader());
        saleInvItemService.save(invId, data.getItemList());
        saveLog(data, headerOld, itemListOld, operate);
    }

    /**
     * 记录修改日志
     */
    private void saveLog(SaleInvDataVO data, SaleInvHeader headerOld, List<SaleInvItem> itemListOld, String operate) {
        SaleInvHeader header = data.getHeader();
        clientCore.tool.clogSaveA(header.getInvId(), "z000_sd_sale_inv_header", operate, header, headerOld);
        clientCore.tool.clogSaveB(header.getInvId(), "itemNo", "z000_sd_sale_inv_item", operate, data.getItemList(), itemListOld);
    }
}

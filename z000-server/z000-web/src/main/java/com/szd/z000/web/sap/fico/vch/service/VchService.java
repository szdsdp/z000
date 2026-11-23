package com.szd.z000.web.sap.fico.vch.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.*;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.security.ClientSecurity;
import com.szd.core.client.util.ClientUtilString;
import com.szd.z000.base.orm.domain.fico.vch.VchAdjust;
import com.szd.z000.base.orm.domain.fico.vch.VchHeader;
import com.szd.z000.base.orm.domain.fico.vch.VchItem;
import com.szd.z000.service.common.utils.Z000Util;
import com.szd.z000.web.sap.fico.vch.domain.VchDataVO;
import com.szd.z000.web.sap.fico.vch.domain.VchParamVO;
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
 * 费用报账-核心服务
 */
@Service
public class VchService {
    private static final Logger logger = LoggerFactory.getLogger(VchService.class);

    @Resource
    private VchHeaderService vchHeaderService;
    @Resource
    private VchItemService vchItemService;
    @Resource
    private VchAdjustService vchAdjustService;
    @Resource
    private VchSapService vchSapService;
    @Resource
    private ClientCore clientCore;
    @Resource
    private VchService vchService;

    public List<VchHeader> selectList(ClientRpParam param) {
        return vchHeaderService.selectList(param);
    }

    public VchDataVO selectData(String costId) {
        VchDataVO dataVO = new VchDataVO();
        VchHeader header = vchHeaderService.selectById(costId);
        dataVO.setHeader(header);
        dataVO.setItemList(vchItemService.selectList(costId));
        dataVO.setAdjustList(vchAdjustService.selectList(costId));
        return dataVO;
    }

    public ClientResult check(VchParamVO paramVO) {
        ClientResult ret = new ClientResult();
        checkData(paramVO);
        ret.setStatus("S");
        ret.setMsg("检查通过");
        return ret;
    }

    private void checkData(VchParamVO paramVO) {
        VchDataVO data = paramVO.getData();
        VchHeader header = data.getHeader();
        ClientResult clientResult = clientCore.wf.wfCheckBtn(header.getCostId(), ClientSecurity.getUserId(), "U", paramVO.getWfEvt().getOperate());
        if (clientResult != null && "E".equals(clientResult.getStatus())) {
            throw new ClientCustomException(clientResult.getMsg());
        }
        vchHeaderService.checkData(header);
    }

    @Transactional(rollbackFor = Exception.class)
    public ClientWfReturn saveData(VchParamVO paramVO) {
        ClientWfReturn clientWfReturn = new ClientWfReturn();
        if ("btn_delete".equals(paramVO.getWfEvt().getOperate())) {
            paramVO.setData(selectData(paramVO.getData().getHeader().getCostId()));
        } else {
            checkData(paramVO);
        }
        saveBussWfData(paramVO, clientWfReturn);
        // 审批完成后调用SAP接口
        ClientResult clientResult = vchSapService.genSapVch(paramVO.getData().getHeader().getCostId());
        if (ClientUtilString.isNotBlank(clientResult.getStatus())) {
            if (CollectionUtils.isEmpty(clientWfReturn.getRetMsgBox())) {
                clientWfReturn.setRetMsgBox(new ArrayList<>());
            }
            clientWfReturn.getRetMsgBox().addAll(clientResult.getRetMsgBox());
        }
        return clientWfReturn;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveBussWfData(VchParamVO paramVO, ClientWfReturn clientWfReturn) {
        saveBussData(paramVO);
        ClientWfReturn wfReturn = approval(paramVO.getData(), paramVO.getWfEvt());
        ClientWfBussBase wfBussBase = wfReturn.getWfBussBase();
        BeanUtils.copyProperties(wfReturn, clientWfReturn);
        clientWfReturn.setBussId(paramVO.getData().getHeader().getCostId());
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
                vchHeaderService.updateStatusAndDocId(paramVO.getData().getHeader());
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveBussData(VchParamVO paramVO) {
        try {
            VchDataVO data = paramVO.getData();
            VchHeader header = data.getHeader();
            if (StringUtils.isBlank(header.getCostId())) {
                String bussId = clientCore.tool.getNumSeq();
                header.setCostId(bussId);
                header.setBussId(bussId);
                header.setCreateBy(ClientSecurity.getUserId());
                header.setCreateTime(new Date());
                header.setBussStatus("A");
                Z000Util.saveBussBase(header);
                vchService.insert(data);
            } else {
                header.setUpdateBy(ClientSecurity.getUserId());
                header.setUpdateTime(new Date());
                Z000Util.saveBussBase(header);
                vchService.update(data, paramVO.getWfEvt().getOperate());
            }
        } catch (Exception e) {
            logger.error("保存失败", e);
            throw new ClientCustomException("保存失败:" + e.getMessage());
        }
    }

    public ClientWfReturn approval(VchDataVO data, ClientWfParamEvt wfEvt) {
        ClientWfReturn wfReturn = new ClientWfReturn();
        try {
            VchHeader header = data.getHeader();
            String bussId = header.getCostId();
            ClientWfParamBuss wfParamBuss = Z000Util.setWfParamBuss(header, bussId);
            ClientWfParam wfParam = new ClientWfParam(wfEvt, wfParamBuss);
            wfReturn = clientCore.wf.wfCall(wfParam);
        } catch (Exception e) {
            wfReturn.setRetCodeF("E");
            wfReturn.setRetMsg(e.getMessage());
        }
        return wfReturn;
    }

    private void deleteById(String costId) {
        vchHeaderService.deleteById(costId);
        vchItemService.deleteByCostId(costId);
        vchAdjustService.deleteByCostId(costId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insert(VchDataVO data) {
        VchHeader header = data.getHeader();
        vchHeaderService.insert(header);
        vchItemService.save(header.getCostId(), data.getItemList());
        vchAdjustService.save(header.getCostId(), data.getAdjustList(), "A");
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(VchDataVO data, String operate) {
        String costId = data.getHeader().getCostId();
        VchHeader headerOld = vchHeaderService.selectObjById(costId);
        List<VchItem> itemListOld = vchItemService.selectList(costId);
        List<VchAdjust> adjustListOld = vchAdjustService.selectList(costId);
        vchHeaderService.update(data.getHeader());
        vchItemService.save(costId, data.getItemList());
        vchAdjustService.save(costId, data.getAdjustList(), operate);
        saveLog(data, headerOld, itemListOld, adjustListOld, operate);
    }

    private void saveLog(VchDataVO data, VchHeader headerOld, List<VchItem> itemListOld, List<VchAdjust> adjustListOld, String operate) {
        VchHeader header = data.getHeader();
        clientCore.tool.clogSaveA(header.getCostId(), "z000_fi_vch_header", operate, header, headerOld);
        clientCore.tool.clogSaveB(header.getCostId(), "itemNo", "z000_fi_vch_item", operate, data.getItemList(), itemListOld);
        clientCore.tool.clogSaveB(header.getCostId(), "itemNo", "z000_fi_vch_adjust", operate, data.getAdjustList(), adjustListOld);
    }
}

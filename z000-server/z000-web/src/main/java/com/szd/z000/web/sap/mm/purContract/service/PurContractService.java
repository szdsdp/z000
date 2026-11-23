package com.szd.z000.web.sap.mm.purContract.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.*;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.security.ClientSecurity;
import com.szd.core.client.util.ClientUtilString;
import com.szd.z000.base.orm.domain.mm.purContract.PurContractHeader;
import com.szd.z000.base.orm.domain.mm.purContract.PurContractItem;
import com.szd.z000.service.common.utils.Z000Util;
import com.szd.z000.web.sap.mm.purContract.domain.PurContractDataVO;
import com.szd.z000.web.sap.mm.purContract.domain.PurContractParamVO;
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
 * 采购合同-服务类
 */
@Service
public class PurContractService {
    private static final Logger logger = LoggerFactory.getLogger(PurContractService.class);

    @Resource
    private PurContractHeaderService purContractHeaderService;
    @Resource
    private PurContractItemService purContractItemService;
    @Resource
    private PurContractSapService purContractSapService;
    @Resource
    private ClientCore clientCore;
    @Resource
    private PurContractService purContractService;

    public List<PurContractHeader> selectList(ClientRpParam param) {
        return purContractHeaderService.selectList(param);
    }

    public PurContractDataVO selectData(String contractId) {
        PurContractDataVO dataVO = new PurContractDataVO();
        PurContractHeader header = purContractHeaderService.selectById(contractId);
        dataVO.setHeader(header);
        dataVO.setItemList(purContractItemService.selectList(contractId));
        return dataVO;
    }

    public ClientResult check(PurContractParamVO paramVO) {
        ClientResult ret = new ClientResult();
        checkData(paramVO);
        ret.setStatus("S");
        ret.setMsg("检查通过");
        return ret;
    }

    private void checkData(PurContractParamVO paramVO) {
        PurContractDataVO data = paramVO.getData();
        PurContractHeader header = data.getHeader();
        ClientResult clientResult = clientCore.wf.wfCheckBtn(header.getContractId(), ClientSecurity.getUserId(), "U", paramVO.getWfEvt().getOperate());
        if (clientResult != null && "E".equals(clientResult.getStatus())) {
            throw new ClientCustomException(clientResult.getMsg());
        }
        purContractHeaderService.checkData(header);
    }

    public ClientWfReturn saveData(PurContractParamVO paramVO) {
        ClientWfReturn clientWfReturn = new ClientWfReturn();
        if ("btn_delete".equals(paramVO.getWfEvt().getOperate())) {
            paramVO.setData(selectData(paramVO.getData().getHeader().getContractId()));
        } else {
            checkData(paramVO);
        }
        saveBussWfData(paramVO, clientWfReturn);
        ClientResult clientResult = purContractSapService.genSapPurContract(paramVO.getData().getHeader().getContractId());
        if (ClientUtilString.isNotBlank(clientResult.getStatus())) {
            if (CollectionUtils.isEmpty(clientWfReturn.getRetMsgBox())) {
                clientWfReturn.setRetMsgBox(new ArrayList<>());
            }
            clientWfReturn.getRetMsgBox().addAll(clientResult.getRetMsgBox());
        }
        return clientWfReturn;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveBussWfData(PurContractParamVO paramVO, ClientWfReturn clientWfReturn) {
        saveBussData(paramVO);
        ClientWfReturn wfReturn = approval(paramVO.getData(), paramVO.getWfEvt());
        ClientWfBussBase wfBussBase = wfReturn.getWfBussBase();
        BeanUtils.copyProperties(wfReturn, clientWfReturn);
        clientWfReturn.setBussId(paramVO.getData().getHeader().getContractId());
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
                purContractHeaderService.updateStatusAndDocId(paramVO.getData().getHeader());
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveBussData(PurContractParamVO paramVO) {
        try {
            PurContractDataVO data = paramVO.getData();
            PurContractHeader header = data.getHeader();
            if (StringUtils.isBlank(header.getContractId())) {
                String bussId = clientCore.tool.getNumSeq();
                header.setContractId(bussId);
                header.setBussId(bussId);
                header.setCreateBy(ClientSecurity.getUserId());
                header.setCreateTime(new Date());
                header.setBussStatus("A");
                Z000Util.saveBussBase(header);
                purContractService.insert(data);
            } else {
                header.setUpdateBy(ClientSecurity.getUserId());
                header.setUpdateTime(new Date());
                Z000Util.saveBussBase(header);
                purContractService.update(data, paramVO.getWfEvt().getOperate());
            }
        } catch (Exception e) {
            logger.error("保存失败", e);
            throw new ClientCustomException("保存失败:" + e.getMessage());
        }
    }

    public ClientWfReturn approval(PurContractDataVO data, ClientWfParamEvt wfEvt) {
        ClientWfReturn wfReturn = new ClientWfReturn();
        try {
            PurContractHeader header = data.getHeader();
            String bussId = header.getContractId();
            ClientWfParamBuss wfParamBuss = Z000Util.setWfParamBuss(header, bussId);
            ClientWfParam wfParam = new ClientWfParam(wfEvt, wfParamBuss);
            wfReturn = clientCore.wf.wfCall(wfParam);
        } catch (Exception e) {
            wfReturn.setRetCodeF("E");
            wfReturn.setRetMsg(e.getMessage());
        }
        return wfReturn;
    }

    private void deleteById(String contractId) {
        purContractHeaderService.deleteById(contractId);
        purContractItemService.deleteByContractId(contractId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insert(PurContractDataVO data) {
        PurContractHeader header = data.getHeader();
        purContractHeaderService.insert(header);
        purContractItemService.save(header.getContractId(), data.getItemList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(PurContractDataVO data, String operate) {
        String contractId = data.getHeader().getContractId();
        PurContractHeader headerOld = purContractHeaderService.selectObjById(contractId);
        purContractHeaderService.update(data.getHeader());
        // 查询历史行项目（在save覆盖前查询）
        List<PurContractItem> itemListOld = purContractItemService.selectList(contractId);
        purContractItemService.save(contractId, data.getItemList());
        saveLog(data, headerOld, itemListOld, operate);
    }

    private void saveLog(PurContractDataVO data, PurContractHeader headerOld, List<PurContractItem> itemListOld, String operate) {
        PurContractHeader header = data.getHeader();
        clientCore.tool.clogSaveA(header.getContractId(), "z000_mm_pur_contract_header", operate, header, headerOld);
        clientCore.tool.clogSaveB(header.getContractId(), "itemNo", "z000_mm_pur_contract_item", operate, data.getItemList(), itemListOld);
    }
}

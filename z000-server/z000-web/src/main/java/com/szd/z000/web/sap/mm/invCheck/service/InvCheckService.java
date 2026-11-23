package com.szd.z000.web.sap.mm.invCheck.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.*;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.security.ClientSecurity;
import com.szd.core.client.util.ClientUtilDate;
import com.szd.core.client.util.ClientUtilString;
import com.szd.z000.base.orm.domain.mm.invCheck.InvCheckHeader;
import com.szd.z000.base.orm.domain.mm.invCheck.InvCheckItem;
import com.szd.z000.service.common.utils.Z000Util;
import com.szd.z000.web.sap.mm.invCheck.domain.InvCheckDataVO;
import com.szd.z000.web.sap.mm.invCheck.domain.InvCheckParamVO;
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
 * 发票校验-服务类
 */
@Service
public class InvCheckService {
    private static final Logger logger = LoggerFactory.getLogger(InvCheckService.class);

    @Resource
    private InvCheckHeaderService invCheckHeaderService;
    @Resource
    private InvCheckItemService invCheckItemService;
    @Resource
    private InvCheckSapService invCheckSapService;
    @Resource
    private ClientCore clientCore;
    @Resource
    private InvCheckService invCheckService;

    public List<InvCheckHeader> selectList(ClientRpParam param) {
        return invCheckHeaderService.selectList(param);
    }

    public InvCheckDataVO selectData(String invCheckId) {
        InvCheckDataVO dataVO = new InvCheckDataVO();
        dataVO.setHeader(invCheckHeaderService.selectById(invCheckId));
        dataVO.setItemList(invCheckItemService.selectList(invCheckId));
        return dataVO;
    }

    public ClientResult check(InvCheckParamVO paramVO) {
        ClientResult ret = new ClientResult();
        checkData(paramVO);
        ret.setStatus("S");
        ret.setMsg("检查通过");
        return ret;
    }

    private void checkData(InvCheckParamVO paramVO) {
        InvCheckHeader header = paramVO.getData().getHeader();
        ClientResult clientResult = clientCore.wf.wfCheckBtn(header.getInvId(), ClientSecurity.getUserId(), "U", paramVO.getWfEvt().getOperate());
        if (clientResult != null && "E".equals(clientResult.getStatus())) {
            throw new ClientCustomException(clientResult.getMsg());
        }
        invCheckHeaderService.checkData(header);
        // 后台校验数量是否超限
        invCheckItemService.checkData(paramVO.getData().getItemList(), header.getInvId());
    }

    public ClientWfReturn saveData(InvCheckParamVO paramVO) {
        ClientWfReturn clientWfReturn = new ClientWfReturn();
        if ("btn_delete".equals(paramVO.getWfEvt().getOperate())) {
            paramVO.setData(selectData(paramVO.getData().getHeader().getInvId()));
        } else {
            checkData(paramVO);
        }
        saveBussWfData(paramVO, clientWfReturn);
        ClientResult clientResult = invCheckSapService.genSapInvCheck(paramVO.getData().getHeader().getInvId());
        if (ClientUtilString.isNotBlank(clientResult.getStatus())) {
            if (CollectionUtils.isEmpty(clientWfReturn.getRetMsgBox())) {
                clientWfReturn.setRetMsgBox(new ArrayList<>());
            }
            clientWfReturn.getRetMsgBox().addAll(clientResult.getRetMsgBox());
        }
        return clientWfReturn;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveBussWfData(InvCheckParamVO paramVO, ClientWfReturn clientWfReturn) {
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
                invCheckHeaderService.updateStatusAndDocId(paramVO.getData().getHeader());
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveBussData(InvCheckParamVO paramVO) {
        try {
            InvCheckDataVO data = paramVO.getData();
            InvCheckHeader header = data.getHeader();
            if (StringUtils.isBlank(header.getInvId())) {
                String bussId = clientCore.tool.getNumSeq();
                header.setInvId(bussId);
                header.setBussId(bussId);
                header.setCreateBy(ClientSecurity.getUserId());
                header.setCreateTime(new Date());
                header.setBussStatus("A");
                Z000Util.saveBussBase(header);
                invCheckService.insert(data);
            } else {
                header.setUpdateBy(ClientSecurity.getUserId());
                header.setUpdateTime(new Date());
                Z000Util.saveBussBase(header);
                invCheckService.update(data, paramVO.getWfEvt().getOperate());
            }
        } catch (Exception e) {
            logger.error("保存失败", e);
            throw new ClientCustomException("保存失败:" + e.getMessage());
        }
    }

    public ClientWfReturn approval(InvCheckDataVO data, ClientWfParamEvt wfEvt) {
        ClientWfReturn wfReturn = new ClientWfReturn();
        try {
            InvCheckHeader header = data.getHeader();
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

    private void deleteById(String invCheckId) {
        invCheckHeaderService.deleteById(invCheckId);
        invCheckItemService.deleteByInvCheckId(invCheckId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insert(InvCheckDataVO data) {
        InvCheckHeader header = data.getHeader();
        invCheckHeaderService.insert(header);
        invCheckItemService.save(header.getInvId(), data.getItemList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(InvCheckDataVO data, String operate) {
        String invCheckId = data.getHeader().getInvId();
        InvCheckHeader headerOld = invCheckHeaderService.selectObjById(invCheckId);
        invCheckHeaderService.update(data.getHeader());
        // 查询历史行项目（在save覆盖前查询）
        List<InvCheckItem> itemListOld = invCheckItemService.selectList(invCheckId);
        invCheckItemService.save(invCheckId, data.getItemList());
        saveLog(data, headerOld, itemListOld, operate);
    }

    /**
     * 从物料凭证创建发票校验(生成A状态草稿)
     */
    @Transactional(rollbackFor = Exception.class)
    public String createFromCgrk(InvCheckParamVO paramVO) {
        InvCheckDataVO data = paramVO.getData();
        InvCheckHeader header = data.getHeader();
        // 校验：物料凭证行项目是否已生成过发票校验
        if (!CollectionUtils.isEmpty(data.getItemList())) {
            List<String> duplicates = invCheckItemService.checkDuplicate(data.getItemList());
            if (!duplicates.isEmpty()) {
                throw new ClientCustomException("以下订单行的发票校验数量已达物料凭证上限，不可再生成：" + String.join("、", duplicates));
            }
        }
        // 生成单据ID
        String bussId = clientCore.tool.getNumSeq();
        header.setInvId(bussId);
        header.setBussId(bussId);
        header.setCreateBy(ClientSecurity.getUserId());
        header.setCreateTime(new Date());
        header.setBussStatus("A");
        header.setTitle("发票校验_" + ClientUtilDate.getDate("yyyy-MM-dd", new Date()));
        header.setBussDate(new Date());
        // 设置行项目号
        if (!CollectionUtils.isEmpty(data.getItemList())) {
            for (int i = 0; i < data.getItemList().size(); i++) {
                data.getItemList().get(i).setItemNo(i + 1);
            }
        }
        Z000Util.saveBussBase(header);
        invCheckService.insert(data);
        return bussId;
    }

    private void saveLog(InvCheckDataVO data, InvCheckHeader headerOld, List<InvCheckItem> itemListOld, String operate) {
        InvCheckHeader header = data.getHeader();
        clientCore.tool.clogSaveA(header.getInvId(), "z000_mm_inv_check_header", operate, header, headerOld);
        clientCore.tool.clogSaveB(header.getInvId(), "itemNo", "z000_mm_inv_check_item", operate, data.getItemList(), itemListOld);
    }
}

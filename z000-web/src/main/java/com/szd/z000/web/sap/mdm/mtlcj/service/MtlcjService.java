package com.szd.z000.web.sap.mdm.mtlcj.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.*;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.security.ClientSecurity;
import com.szd.core.client.util.ClientUtilString;
import com.szd.z000.base.orm.domain.mdm.mtlcj.MtlcjHeader;
import com.szd.z000.base.orm.mapper.mdm.mtlcj.MtlcjBaseViewMapper;
import com.szd.z000.base.orm.mapper.mdm.mtlcj.MtlcjHeaderMapper;
import com.szd.z000.base.orm.mapper.mdm.mtlcj.MtlcjPurViewMapper;
import com.szd.z000.service.common.utils.Z000Util;
import com.szd.z000.web.sap.mdm.mtlcj.domain.MtlcjDataVO;
import com.szd.z000.web.sap.mdm.mtlcj.domain.MtlcjParamVO;
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
 * 物料主数据创建申请-服务类
 */
@Service
public class MtlcjService {
    private static final Logger logger = LoggerFactory.getLogger(MtlcjService.class);

    @Resource
    private MtlcjHeaderMapper mtlcjHeaderMapper;
    @Resource
    private MtlcjBaseViewMapper mtlcjBaseViewMapper;
    @Resource
    private MtlcjPurViewMapper mtlcjPurViewMapper;
    @Resource
    private MtlcjSapService mtlcjSapService;
    @Resource
    private ClientCore clientCore;
    @Resource
    private MtlcjService mtlcjService;

    /**
     * 列表查询
     */
    public List<MtlcjHeader> selectList(ClientRpParam param) {
        return mtlcjHeaderMapper.selectList(param);
    }

    /**
     * 详情查询
     */
    public MtlcjDataVO selectData(String applyId) {
        MtlcjDataVO dataVO = new MtlcjDataVO();
        dataVO.setHeader(mtlcjHeaderMapper.selectById(applyId));
        dataVO.setBaseView(mtlcjBaseViewMapper.selectByApplyId(applyId));
        dataVO.setPurView(mtlcjPurViewMapper.selectByApplyId(applyId));
        return dataVO;
    }

    /**
     * 数据检查
     */
    public ClientResult check(MtlcjParamVO paramVO) {
        ClientResult ret = new ClientResult();
        checkData(paramVO);
        ret.setStatus("S");
        ret.setMsg("检查通过");
        return ret;
    }

    private void checkData(MtlcjParamVO paramVO) {
        MtlcjHeader header = paramVO.getData().getHeader();
        ClientResult clientResult = clientCore.wf.wfCheckBtn(header.getApplyId(), ClientSecurity.getUserId(), "U", paramVO.getWfEvt().getOperate());
        if (clientResult != null && "E".equals(clientResult.getStatus())) {
            throw new ClientCustomException(clientResult.getMsg());
        }
        // 物料描述必填
        if (StringUtils.isBlank(header.getMtlName())) {
            throw new ClientCustomException("物料名称不能为空");
        }
    }

    /**
     * 数据保存
     */
    public ClientWfReturn saveData(MtlcjParamVO paramVO) {
        ClientWfReturn clientWfReturn = new ClientWfReturn();
        // 作废不校验
        if ("btn_delete".equals(paramVO.getWfEvt().getOperate())) {
            // 重新查询数据库数据
            paramVO.setData(selectData(paramVO.getData().getHeader().getApplyId()));
        } else {
            // 检查业务数据
            checkData(paramVO);
        }
        // 保存业务数据
        saveBussWfData(paramVO, clientWfReturn);
        // 推送SAP
        ClientResult clientResult = mtlcjSapService.genSapMtlcj(paramVO.getData().getHeader().getApplyId());
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
    public void saveBussWfData(MtlcjParamVO paramVO, ClientWfReturn clientWfReturn) {
        // 保存业务数据
        saveBussData(paramVO);
        // 审批
        ClientWfReturn wfReturn = approval(paramVO.getData(), paramVO.getWfEvt());
        // 获取业务主体
        ClientWfBussBase wfBussBase = wfReturn.getWfBussBase();
        // 复制工作流返回消息
        BeanUtils.copyProperties(wfReturn, clientWfReturn);
        clientWfReturn.setBussId(paramVO.getData().getHeader().getApplyId());
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
                mtlcjHeaderMapper.updateStatusAndDocId(paramVO.getData().getHeader().getApplyId(), bussStatus, wfReturn.getBussDocId());
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveBussData(MtlcjParamVO paramVO) {
        try {
            MtlcjDataVO data = paramVO.getData();
            MtlcjHeader header = data.getHeader();
            if (StringUtils.isBlank(header.getApplyId())) {
                // 新增
                String bussId = clientCore.tool.getNumSeq();
                header.setApplyId(bussId);
                header.setBussId(bussId);
                header.setCreateBy(ClientSecurity.getUserId());
                header.setCreateTime(new Date());
                header.setBussStatus("A");
                Z000Util.saveBussBase(header);
                mtlcjService.insert(data);
            } else {
                // 修改
                header.setUpdateBy(ClientSecurity.getUserId());
                header.setUpdateTime(new Date());
                Z000Util.saveBussBase(header);
                mtlcjService.update(data);
            }
        } catch (Exception e) {
            logger.error("保存失败", e);
            throw new ClientCustomException("保存失败:" + e.getMessage());
        }
    }

    /**
     * 流程审批
     */
    public ClientWfReturn approval(MtlcjDataVO data, ClientWfParamEvt wfEvt) {
        ClientWfReturn wfReturn = new ClientWfReturn();
        try {
            MtlcjHeader header = data.getHeader();
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

    /**
     * 新增
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(MtlcjDataVO data) {
        MtlcjHeader header = data.getHeader();
        mtlcjHeaderMapper.insert(header);
        if (data.getBaseView() != null) {
            data.getBaseView().setApplyId(header.getApplyId());
            mtlcjBaseViewMapper.insert(data.getBaseView());
        }
        if (data.getPurView() != null) {
            data.getPurView().setApplyId(header.getApplyId());
            mtlcjPurViewMapper.insert(data.getPurView());
        }
    }

    /**
     * 修改
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(MtlcjDataVO data) {
        String applyId = data.getHeader().getApplyId();
        mtlcjHeaderMapper.update(data.getHeader());
        // 基础视图：先删后插
        mtlcjBaseViewMapper.deleteByApplyId(applyId);
        if (data.getBaseView() != null) {
            data.getBaseView().setApplyId(applyId);
            mtlcjBaseViewMapper.insert(data.getBaseView());
        }
        // 采购视图：先删后插
        mtlcjPurViewMapper.deleteByApplyId(applyId);
        if (data.getPurView() != null) {
            data.getPurView().setApplyId(applyId);
            mtlcjPurViewMapper.insert(data.getPurView());
        }
    }

    /**
     * 删除
     */
    private void deleteById(String applyId) {
        mtlcjHeaderMapper.deleteById(applyId);
        mtlcjBaseViewMapper.deleteByApplyId(applyId);
        mtlcjPurViewMapper.deleteByApplyId(applyId);
    }

}

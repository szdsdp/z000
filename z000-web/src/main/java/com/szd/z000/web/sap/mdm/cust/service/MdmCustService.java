package com.szd.z000.web.sap.mdm.cust.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.*;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.security.ClientSecurity;
import com.szd.core.client.util.ClientUtilString;
import com.szd.z000.base.orm.domain.mdm.cust.*;
import com.szd.z000.service.common.utils.Z000Util;
import com.szd.z000.web.sap.mdm.cust.domain.MdmCustDataVO;
import com.szd.z000.web.sap.mdm.cust.domain.MdmCustParamVO;
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
import java.util.stream.Collectors;

/**
 * 客商申请-服务类
 */
@Service
public class MdmCustService {
    private static final Logger logger = LoggerFactory.getLogger(MdmCustService.class);
    @Resource
    private MdmCustHeaderService mdmCustHeaderService;
    @Resource
    private MdmCustCmpyService mdmCustCmpyService;
    @Resource
    private MdmCustBkacService mdmCustBkacService;
    @Resource
    private MdmCustSaleService mdmCustSaleService;
    @Resource
    private MdmCustPurchaseService mdmCustPurchaseService;
    @Resource
    private MdmCustCreditService mdmCustCreditService;
    @Resource
    private ClientCore clientCore;
    @Resource
    private MdmCustService mdmCustService;
//    @Resource
//    private AsmRegionMapper asmRegionMapper;
    @Resource
    private MdmCustSapService mdmCustSapService;

    public List<MdmCustHeader> selectList(ClientRpParam param) {
        return mdmCustHeaderService.selectList(param);
    }

    public MdmCustDataVO selectData(String applyId) {
        MdmCustDataVO MdmCustDataVO = new MdmCustDataVO();
        MdmCustHeader header = mdmCustHeaderService.selectById(applyId);
        List<MdmCustCmpy> cmpyList = mdmCustCmpyService.selectList(applyId);
        List<MdmCustBkac> bkacList = mdmCustBkacService.selectList(applyId);
        List<MdmCustSale> saleList = mdmCustSaleService.selectList(applyId);
        List<MdmCustPurchase> purchaseList = mdmCustPurchaseService.selectList(applyId);
        List<MdmCustCredit> creditList = mdmCustCreditService.selectList(applyId);

        MdmCustDataVO.setHeader(header);
        MdmCustDataVO.setCmpyKList(cmpyList.stream().filter(c -> "K".equals(c.getCustFlag())).collect(Collectors.toList()));
        MdmCustDataVO.setCmpyDList(cmpyList.stream().filter(c -> "D".equals(c.getCustFlag())).collect(Collectors.toList()));
        MdmCustDataVO.setBkacList(bkacList);
        MdmCustDataVO.setSaleList(saleList);
        MdmCustDataVO.setPurchaseList(purchaseList);
        MdmCustDataVO.setCreditList(creditList);
        return MdmCustDataVO;
    }

    public ClientResult check(MdmCustParamVO MdmCustParamVO) {
        ClientResult ret = new ClientResult();
        checkData(MdmCustParamVO);
        ret.setStatus("S");
        ret.setMsg("检查通过");
        return ret;
    }

    /**
     * 业务数据校验
     */
    private void checkData(MdmCustParamVO MdmCustParamVO) {
        MdmCustDataVO data = MdmCustParamVO.getData();
        MdmCustHeader header = data.getHeader();
        // 工作流按钮权限检查
        ClientResult clientResult = clientCore.wf.wfCheckBtn(header.getBussId(), ClientSecurity.getUserId(), "U", MdmCustParamVO.getWfEvt().getOperate());
        if (clientResult != null && "E".equals(clientResult.getStatus())) {
            throw new ClientCustomException(clientResult.getMsg());
        }
        // 检查基本信息
        mdmCustHeaderService.checkData(data);
    }

    /**
     * 数据保存
     */
    public ClientWfReturn saveData(MdmCustParamVO mdmCustParamVO) {
        ClientWfReturn clientWfReturn = new ClientWfReturn();
        // 作废不校验
        if ("btn_delete".equals(mdmCustParamVO.getWfEvt().getOperate())) {
            // 重新查询数据库数据
            mdmCustParamVO.setData(selectData(mdmCustParamVO.getData().getHeader().getApplyId()));
        } else {
            // 检查业务数据
            checkData(mdmCustParamVO);
        }
        // 保存业务数据
        saveBussWfData(mdmCustParamVO, clientWfReturn);
        // 推送SAP
        ClientResult clientResult = mdmCustSapService.genSapCust(mdmCustParamVO.getData().getHeader().getApplyId());
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
    public void saveBussWfData(MdmCustParamVO mdmCustParamVO, ClientWfReturn clientWfReturn) {
        // 保存业务数据
        saveBussData(mdmCustParamVO);
        // 查询
//        if (sapCustParamVO.getData().getHeader().getCustFlagDB().contains("K") &&
//                "btn_commit".equals(sapCustParamVO.getWfEvt().getOperate())) {
//            if (!CollectionUtils.isEmpty(sapCustParamVO.getData().getSaleList())) {
//                String saleRegion = sapCustParamVO.getData().getSaleList().get(0).getSaleRegion();
//                AsmRegion asmRegion = asmRegionMapper.selectById(saleRegion);
//                if (asmRegion != null && ClientUtilString.isNotBlank(asmRegion.getNqUser())) {
//                    String nextUserId = asmRegion.getNqUser();
//                    if (ClientUtilString.isNotBlank(nextUserId)) {
//                        sapCustParamVO.getWfEvt().setNextUserId(nextUserId);
//                    }
//                }
//            }
//        }
        if (!CollectionUtils.isEmpty(mdmCustParamVO.getData().getSaleList())) {
            mdmCustParamVO.getData().getHeader().setRegionCode(mdmCustParamVO.getData().getSaleList().get(0).getSaleRegion());
        }
        // 审批
        ClientWfReturn wfReturn = approval(mdmCustParamVO.getData(), mdmCustParamVO.getWfEvt());
        // 获取业务主体
        ClientWfBussBase wfBussBase = wfReturn.getWfBussBase();
        // 复制工作流返回消息
        BeanUtils.copyProperties(wfReturn, clientWfReturn);
        clientWfReturn.setBussId(mdmCustParamVO.getData().getHeader().getApplyId());
        clientWfReturn.setBussDocId(mdmCustParamVO.getData().getHeader().getBussDocId());
        // 根据工作流状态更新业务单据
        if (!"E".equals(wfReturn.getRetCodeF())) {
            // 草稿状态的单据，作废时直接删除
            if ("btn_delete".equals(mdmCustParamVO.getWfEvt().getOperate()) && "A".equals(mdmCustParamVO.getData().getHeader().getBussStatus())) {
                deleteById(clientWfReturn.getBussId());
            } else {
                // 工作流状态转换为业务状态
                String bussStatus = Z000Util.getBussStatus(wfBussBase.getStatus(), wfBussBase.getRejectStatus());
                mdmCustParamVO.getData().getHeader().setBussStatus(bussStatus);
                mdmCustParamVO.getData().getHeader().setBussDocId(wfReturn.getBussDocId());
                mdmCustHeaderService.updateStatusAndDocId(mdmCustParamVO.getData().getHeader());
            }
        }
    }

    /**
     * 保存草稿
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveBussData(MdmCustParamVO MdmCustParamVO) {
        try {
            // 业务数据
            MdmCustDataVO data = MdmCustParamVO.getData();
            MdmCustHeader header = data.getHeader();
            // 首次保存
            if (StringUtils.isBlank(header.getApplyId())) {
                // 单据id
                String bussId = clientCore.tool.getNumSeq();
                header.setApplyId(bussId);
                header.setBussId(bussId);
                header.setCreateBy(ClientSecurity.getUserId());
                header.setCreateTime(new Date());
                header.setBussStatus("A");
                // 保存业务主体
                Z000Util.saveBussBase(header);
                // 新增
                mdmCustService.insert(data, MdmCustParamVO.getWfEvt().getOperate());
            } else {
                header.setUpdateBy(ClientSecurity.getUserId());
                header.setUpdateTime(new Date());
                // 保存业务主体
                Z000Util.saveBussBase(header);
                // 更新
                mdmCustService.update(data, MdmCustParamVO.getWfEvt().getOperate());
            }
        } catch (Exception e) {
            logger.error("保存失败", e);
            throw new ClientCustomException("保存失败:" + e.getMessage());
        }
    }

    /**
     * 流程审批
     */
    public ClientWfReturn approval(MdmCustDataVO data, ClientWfParamEvt wfEvt) {
        ClientWfReturn wfReturn = new ClientWfReturn();
        try {
            MdmCustHeader header = data.getHeader();
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

    private void deleteById(String bussId) {
        mdmCustHeaderService.deleteById(bussId);
        mdmCustCmpyService.deleteByBussId(bussId);
        mdmCustBkacService.deleteByBussId(bussId);
        mdmCustSaleService.deleteByBussId(bussId);
        mdmCustPurchaseService.deleteByBussId(bussId);
        mdmCustCreditService.deleteByBussId(bussId);
    }

    /**
     * 业务数据新增
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(MdmCustDataVO data, String operate) {
        MdmCustHeader header = data.getHeader();
        mdmCustHeaderService.insert(header);
        mdmCustCmpyService.save(header.getApplyId(), "K", data.getCmpyKList());
        mdmCustCmpyService.save(header.getApplyId(), "D", data.getCmpyDList());
        mdmCustBkacService.save(header.getApplyId(), data.getBkacList());
        mdmCustSaleService.save(header.getApplyId(), data.getSaleList());
        mdmCustPurchaseService.save(header.getApplyId(), data.getPurchaseList());
        mdmCustCreditService.save(header.getApplyId(), data.getCreditList());
    }

    /**
     * 业务数据更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(MdmCustDataVO data, String operate) {
        String bussId = data.getHeader().getApplyId();
        // 查询历史抬头
        MdmCustHeader headerOld = mdmCustHeaderService.selectById(bussId);
        // 更新抬头
        mdmCustHeaderService.update(data.getHeader());
        // 更新行项目
        mdmCustCmpyService.save(bussId, "K", data.getCmpyKList());
        mdmCustCmpyService.save(bussId, "D", data.getCmpyDList());
        mdmCustBkacService.save(bussId, data.getBkacList());
        mdmCustSaleService.save(bussId, data.getSaleList());
        mdmCustPurchaseService.save(bussId, data.getPurchaseList());
        mdmCustCreditService.save(bussId, data.getCreditList());
        // 保存修改日志
        saveLog(data, headerOld, operate);
    }

    private void saveLog(MdmCustDataVO data, MdmCustHeader headerOld, String operate) {
        MdmCustHeader header = data.getHeader();
        clientCore.tool.clogSaveA(header.getApplyId(), "sap_cust_header", operate, header, headerOld);
    }

}

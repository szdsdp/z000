package com.szd.z000.web.sap.sd.payRece.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.constants.ClientConstMdm;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.mdm.ClientObjBstp;
import com.szd.core.client.domain.wf.*;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.security.ClientSecurity;
import com.szd.core.client.security.ClientUser;
import com.szd.core.client.util.ClientUtilDate;
import com.szd.core.client.util.ClientUtilString;
import com.szd.z000.base.orm.domain.sd.payFlowBill.PayFlowBill;
import com.szd.z000.base.orm.domain.sd.payFlowCash.PayFlowCash;
import com.szd.z000.base.orm.domain.sd.payRece.PayReceiptBill;
import com.szd.z000.base.orm.domain.sd.payRece.PayReceiptCash;
import com.szd.z000.base.orm.domain.sd.payRece.PayReceiptHeader;
import com.szd.z000.base.orm.domain.sd.payRece.PayReceiptItem;
import com.szd.z000.base.orm.mapper.sd.payFlowBill.PayFlowBillMapper;
import com.szd.z000.base.orm.mapper.sd.payFlowCash.PayFlowCashMapper;
import com.szd.z000.base.orm.mapper.sd.payRece.PayReceiptHeaderMapper;
import com.szd.z000.base.orm.mapper.sd.payRece.PayReceiptItemMapper;
import com.szd.z000.service.common.utils.Z000Util;
import com.szd.z000.web.sap.sd.payRece.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 回款认领服务
 */
@Service
@Slf4j
public class PayReceiptService {
    @Resource
    private PayReceiptHeaderMapper payReceiptHeaderMapper;
    @Resource
    private PayReceiptItemMapper payReceiptItemMapper;
    @Resource
    private PayFlowBillMapper payFlowBillMapper;
    @Resource
    private PayFlowCashMapper payFlowCashMapper;
    @Resource
    private PayReceiptSapService payReceiptSapService;
    @Resource
    private ClientCore clientCore;
    @Resource
    private PayReceiptService payReceiptService;

    /**
     * 查询回款认领清单
     */
    public List<PayReceiptHeader> selectList(com.szd.core.client.domain.common.ClientRpParam param) {
        return payReceiptHeaderMapper.selectList(param);
    }

    /**
     * 查询回款认领详情
     */
    public PayReceDataVO selectData(String bussId) {
        PayReceDataVO payReceDataVO = new PayReceDataVO();
        // 1、查询表头
        PayReceiptHeader header = payReceiptHeaderMapper.selectById(bussId);
        if (header == null) {
            header = new PayReceiptHeader();
        }
        // 2、查询工作流基础数据
        ClientWfBussBase wfBussBase = clientCore.wf.baseGetObj(bussId);
        if (wfBussBase != null) {
            BeanUtils.copyProperties(wfBussBase, header);
        }
        // 3、根据支付方式查询源单数据
        PayReceiptCash cash = new PayReceiptCash();
        PayReceiptBill bill = new PayReceiptBill();
        if ("B".equals(header.getPayWay())) {
            PayFlowCash flow = payFlowCashMapper.selectById(header.getBankDoc());
            if (flow != null) {
                BeanUtils.copyProperties(flow, cash);
                header.setAmt(flow.getAmt());
                header.setAmtRemain(flow.getAmt().subtract(flow.getAmtProcess())
                        .subtract(flow.getAmtReceipt()).add(header.getAmtReceipt()));
            }
            cash.setBankDoc(header.getBankDoc());
        } else {
            PayFlowBill mdmBill = payFlowBillMapper.selectById(header.getBankDoc());
            if (mdmBill != null) {
                BeanUtils.copyProperties(mdmBill, bill);
                bill.setBankDoc(mdmBill.getSysBillNo());
                header.setAmt(mdmBill.getAmt());
                header.setAmtRemain(mdmBill.getAmt().subtract(mdmBill.getAmtProcess())
                        .subtract(mdmBill.getAmtReceipt()).add(header.getAmtReceipt()));
            }
            bill.setBankDoc(header.getBankDoc());
        }
        // 4、查询行项目
        List<PayReceiptItem> itemList = payReceiptItemMapper.selectList(bussId);
        payReceDataVO.setHeader(header);
        payReceDataVO.setCash(cash);
        payReceDataVO.setBill(bill);
        payReceDataVO.setItemList(itemList);
        return payReceDataVO;
    }

    /**
     * 数据检查
     */
    public ClientResult check(PayReceParamVO payReceParamVO) {
        ClientResult ret = new ClientResult();
        checkData(payReceParamVO);
        ret.setStatus("S");
        ret.setMsg("检查通过");
        return ret;
    }

    /**
     * 数据保存（含工作流）
     */
    public ClientWfReturn saveData(PayReceParamVO payReceParamVO) {
        ClientWfReturn clientWfReturn = new ClientWfReturn();
        PayReceiptHeader headerOld = payReceiptHeaderMapper.selectById(payReceParamVO.getData().getHeader().getReceiptId());
        // 作废不校验
        if ("btn_delete".equals(payReceParamVO.getWfEvt().getOperate())) {
            if ("D".equals(headerOld.getBussStatus()) && StringUtils.isNotBlank(headerOld.getVchNum())) {
                throw new ClientCustomException("当前状态:认领成功；不允许作废");
            } else {
                payReceParamVO.setData(selectData(payReceParamVO.getData().getHeader().getReceiptId()));
            }
        } else if ("btn_revoke".equals(payReceParamVO.getWfEvt().getOperate())) {
            if ("D".equals(headerOld.getBussStatus()) && StringUtils.isNotBlank(headerOld.getVchNum())) {
                throw new ClientCustomException("当前状态:认领成功；不允许撤回");
            }
        } else {
            checkData(payReceParamVO);
        }
        // 保存业务数据及工作流
        saveBussWfData(payReceParamVO, clientWfReturn);
        // 推送SAP生成回款认领单
        ClientResult clientResult = payReceiptSapService.createSapPayReceipt(payReceParamVO.getData().getHeader().getReceiptId());
        if (ClientUtilString.isNotBlank(clientResult.getStatus())) {
            if (CollectionUtils.isEmpty(clientWfReturn.getRetMsgBox())) {
                clientWfReturn.setRetMsgBox(new ArrayList<>());
            }
            clientWfReturn.getRetMsgBox().addAll(clientResult.getRetMsgBox());
        }
        return clientWfReturn;
    }

    /**
     * 保存业务数据及工作流
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveBussWfData(PayReceParamVO payReceParamVO, ClientWfReturn clientWfReturn) {
        // 保存业务数据
        saveBussData(payReceParamVO);
        // 审批
        ClientWfReturn wfReturn = approval(payReceParamVO.getData(), payReceParamVO.getWfEvt());
        // 获取业务主体
        ClientWfBussBase wfBussBase = wfReturn.getWfBussBase();
        // 复制工作流返回消息
        BeanUtils.copyProperties(wfReturn, clientWfReturn);
        clientWfReturn.setBussId(payReceParamVO.getData().getHeader().getReceiptId());
        clientWfReturn.setBussDocId(payReceParamVO.getData().getHeader().getBussDocId());
        // 根据工作流状态更新业务单据
        if (!"E".equals(wfReturn.getRetCodeF())) {
            // 草稿状态的单据，作废时直接删除
            if ("btn_delete".equals(payReceParamVO.getWfEvt().getOperate())
                    && ClientUtilString.isBlank(payReceParamVO.getData().getHeader().getBussDocId())
                    && "A".equals(payReceParamVO.getData().getHeader().getBussStatus())) {
                deleteById(clientWfReturn.getBussId());
            } else {
                // 工作流状态转换为业务状态
                String bussStatus = Z000Util.getBussStatus(wfBussBase.getStatus(), wfBussBase.getRejectStatus());
                payReceParamVO.getData().getHeader().setBussStatus(bussStatus);
                payReceParamVO.getData().getHeader().setBussDocId(wfReturn.getBussDocId());
                payReceiptHeaderMapper.updateStatusAndDocId(payReceParamVO.getData().getHeader().getReceiptId(), bussStatus, wfReturn.getBussDocId());
            }
        }
        // 更新源单认领金额
        updateAmtReceipt(payReceParamVO.getData().getHeader());
    }

    /**
     * 删除回款认领单
     */
    private void deleteById(String bussId) {
        payReceiptHeaderMapper.deleteById(bussId);
        payReceiptItemMapper.deleteById(bussId);
    }

    /**
     * 保存业务数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveBussData(PayReceParamVO payReceParamVO) {
        try {
            PayReceDataVO data = payReceParamVO.getData();
            PayReceiptHeader header = data.getHeader();
            // 首次保存
            if (StringUtils.isBlank(header.getReceiptId())) {
                String bussId = clientCore.tool.getNumSeq();
                header.setReceiptId(bussId);
                header.setBussId(bussId);
                header.setCreateBy(ClientSecurity.getUserId());
                header.setCreateTime(new Date());
                header.setBussStatus("A");
                // 保存业务主体
                Z000Util.saveBussBase(header);
                // 新增
                payReceiptService.insert(data);
            } else {
                header.setUpdateBy(ClientSecurity.getUserId());
                header.setUpdateTime(new Date());
                // 保存业务主体
                Z000Util.saveBussBase(header);
                // 更新
                payReceiptService.update(data, payReceParamVO.getWfEvt().getOperate());
            }
        } catch (Exception e) {
            log.error("保存失败", e);
            throw new ClientCustomException("保存失败:" + e.getMessage());
        }
    }

    /**
     * 流程审批
     */
    public ClientWfReturn approval(PayReceDataVO data, ClientWfParamEvt wfEvt) {
        ClientWfReturn wfReturn = new ClientWfReturn();
        try {
            PayReceiptHeader header = data.getHeader();
            // 设置流程字段
            ClientWfParamBuss wfParamBuss = Z000Util.setWfParamBuss(header, header.getBussId());
            // 调用工作流
            ClientWfParam wfParam = new ClientWfParam(wfEvt, wfParamBuss);
            wfReturn = clientCore.wf.wfCall(wfParam);
        } catch (Exception e) {
            wfReturn.setRetCodeF("E");
            wfReturn.setRetMsg(e.getMessage());
        }
        return wfReturn;
    }

    /**
     * 业务数据新增
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(PayReceDataVO data) {
        PayReceiptHeader header = data.getHeader();
        // 保存抬头
        payReceiptHeaderMapper.insert(header);
        // 未清项
        if (!CollectionUtils.isEmpty(data.getItemList())) {
            payReceiptItemMapper.batchInsert(header.getReceiptId(), data.getItemList());
        }
    }

    /**
     * 业务数据更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(PayReceDataVO data, String operate) {
        PayReceiptHeader header = data.getHeader();
        String receiptId = header.getReceiptId();
        // 更新抬头
        payReceiptHeaderMapper.update(header);
        // 未清项：先删后增
        payReceiptItemMapper.deleteById(receiptId);
        if (!CollectionUtils.isEmpty(data.getItemList())) {
            payReceiptItemMapper.batchInsert(receiptId, data.getItemList());
        }
    }

    /**
     * 创建回款认领单（票据/电汇列表快捷入口）
     */
    @Transactional(rollbackFor = Exception.class)
    public CommonApplyResult createPayReceiptDoc(PayReceApplyParam payReceApplyParam) {
        // 1、查询业务类型数据
        ClientObjBstp clientObjBstp = clientCore.mdm.getObjA(ClientConstMdm.CORE_BSTP, payReceApplyParam.getBstp(), ClientObjBstp.class);
        if (ObjectUtils.isEmpty(clientObjBstp)) {
            throw new ClientCustomException("业务类型配置不存在");
        }
        // 2、构建抬头
        PayReceiptHeader header = new PayReceiptHeader();
        String bussId = clientCore.tool.getNumSeq();
        header.setBussId(bussId);
        header.setReceiptId(bussId);
        header.setBstp(payReceApplyParam.getBstp());
        header.setRouterCode(clientObjBstp.getRouterCode());
        header.setBussStatus("A");
        ClientUser user = ClientSecurity.getLoginUser().getUser();
        header.setCmpy(user.getCmpy());
        header.setCstc(user.getCstc());
        header.setPayNature("10");
        header.setCurr(payReceApplyParam.getCurr());
        header.setPayWay(payReceApplyParam.getPayWay());
        header.setUserId(user.getUserId());
        header.setCreateBy(user.getUserId());
        header.setCreateTime(new Date());
        // 3、根据支付方式获取源单数据
        if ("B".equals(payReceApplyParam.getPayWay())) {
            PayFlowCash payFlow = payFlowCashMapper.selectById(payReceApplyParam.getBankDoc());
            if (payFlow == null) {
                throw new ClientCustomException("电汇回款数据不存在");
            }
            header.setBankDoc(payReceApplyParam.getBankDoc());
            header.setBussDate(payFlow.getTranDate());
            header.setCust(payFlow.getCust());
            header.setText("应收_" + payFlow.getPayAccName());
            header.setMyAccNo(payFlow.getMyAccNo());
            header.setPayAccNo(payFlow.getPayAccNo());
            header.setPayAccName(payFlow.getPayAccName());
            header.setSummary(payFlow.getSummary());
            header.setAmtReceipt(payFlow.getAmt().subtract(payFlow.getAmtReceipt()));
        } else {
            PayFlowBill mdmBill = payFlowBillMapper.selectById(payReceApplyParam.getSysBillNo());
            if (mdmBill == null) {
                throw new ClientCustomException("票据承兑数据不存在");
            }
            header.setBankDoc(payReceApplyParam.getSysBillNo());
            header.setBussDate(mdmBill.getSignDate());
            header.setCust(mdmBill.getCust());
            header.setText("应收票据_" + mdmBill.getEndorser());
            header.setMyAccNo(mdmBill.getHolderAcc());
            header.setPayAccNo(mdmBill.getEndorserAcc());
            header.setPayAccName(mdmBill.getEndorser());
            header.setSummary(mdmBill.getSummary());
            header.setAmtReceipt(mdmBill.getAmt().subtract(mdmBill.getAmtReceipt()));
        }
        header.setTitle(ClientUtilDate.getDate("yyyy-MM-dd", header.getBussDate()) + "回款认领");
        if (header.getAmtReceipt().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ClientCustomException("回单已认领，不允许重复认领");
        }
        // 4、保存
        try {
            Z000Util.saveBussBase(header);
            payReceiptHeaderMapper.insert(header);
            updateAmtReceipt(header);
            CommonApplyResult result = new CommonApplyResult();
            result.setBussId(bussId);
            result.setRouterCode(header.getRouterCode());
            return result;
        } catch (Exception e) {
            throw new ClientCustomException("创建回款认领单失败: " + e.getMessage());
        }
    }

    /**
     * 获取认领初始数据
     */
    public PayReceResultVO getDataInit(String bankDocType, String bankDoc) {
        ClientObjBstp clientObjBstp = clientCore.mdm.getObjA(ClientConstMdm.CORE_BSTP, "Z000RKA01", ClientObjBstp.class);
        if (ObjectUtils.isEmpty(clientObjBstp)) {
            throw new ClientCustomException("业务类型Z000RKA01配置不存在");
        }
        PayReceResultVO resultVO = new PayReceResultVO();
        PayReceDataVO dataVO = new PayReceDataVO();
        PayReceiptHeader header = new PayReceiptHeader();
        PayReceiptCash cash = new PayReceiptCash();
        PayReceiptBill bill = new PayReceiptBill();
        header.setBstp(clientObjBstp.getBstp());
        header.setRouterCode(clientObjBstp.getRouterCode());
        header.setBussStatus("A");
        ClientUser user = ClientSecurity.getLoginUser().getUser();
        header.setCmpy(user.getCmpy());
        header.setCstc(user.getCstc());
        header.setPayNature("10");
        header.setUserId(user.getUserId());
        header.setCreateBy(user.getUserId());
        header.setCreateTime(new Date());
        if ("B".equals(bankDocType)) {
            PayFlowCash payFlow = payFlowCashMapper.selectById(bankDoc);
            if (payFlow == null) {
                payFlow = new PayFlowCash();
                payFlow.setBankDoc(bankDoc);
                payFlow.setAmt(BigDecimal.ZERO);
                payFlow.setAmtReceipt(BigDecimal.ZERO);
            }
            header.setPayWay("B");
            header.setCurr(payFlow.getCurr());
            header.setBankDoc(bankDoc);
            header.setBussDate(payFlow.getTranDate());
            header.setCust(payFlow.getCust());
            header.setText("应收_" + payFlow.getPayAccName());
            header.setMyAccNo(payFlow.getMyAccNo());
            header.setPayAccNo(payFlow.getPayAccNo());
            header.setPayAccName(payFlow.getPayAccName());
            header.setSummary(payFlow.getSummary());
            header.setAmt(payFlow.getAmt());
            header.setAmtRemain(payFlow.getAmt().subtract(payFlow.getAmtReceipt()));
            header.setAmtReceipt(header.getAmtRemain());
            BeanUtils.copyProperties(payFlow, cash);
        } else {
            PayFlowBill mdmBill = payFlowBillMapper.selectById(bankDoc);
            if (mdmBill == null) {
                mdmBill = new PayFlowBill();
                mdmBill.setSysBillNo(bankDoc);
                mdmBill.setAmt(BigDecimal.ZERO);
                mdmBill.setAmtReceipt(BigDecimal.ZERO);
            }
            header.setPayWay(mdmBill.getBillType());
            header.setCurr("CNY");
            header.setBankDoc(bankDoc);
            header.setBussDate(mdmBill.getSignDate());
            header.setCust(mdmBill.getCust());
            header.setText("应收票据_" + mdmBill.getEndorser());
            header.setMyAccNo(mdmBill.getHolderAcc());
            header.setPayAccNo(mdmBill.getEndorserAcc());
            header.setPayAccName(mdmBill.getEndorser());
            header.setSummary(mdmBill.getSummary());
            header.setAmt(mdmBill.getAmt());
            header.setAmtRemain(mdmBill.getAmt().subtract(mdmBill.getAmtReceipt()));
            header.setAmtReceipt(header.getAmtRemain());
            BeanUtils.copyProperties(mdmBill, bill);
        }
        header.setTitle(ClientUtilDate.getDate("yyyy-MM-dd", header.getBussDate()) + "回款认领");
        dataVO.setHeader(header);
        dataVO.setCash(cash);
        dataVO.setBill(bill);
        dataVO.setItemList(new ArrayList<>());
        resultVO.setData(dataVO);
        return resultVO;
    }

    /**
     * 更新源单认领金额
     */
    public void updateAmtReceipt(PayReceiptHeader header) {
        BigDecimal sumAmtProcess = payReceiptHeaderMapper.getSumAmtProcess(header.getBankDoc());
        BigDecimal sumAmtReceipt = payReceiptHeaderMapper.getSumAmtReceipt(header.getBankDoc());
        if (sumAmtProcess == null) {
            sumAmtProcess = BigDecimal.ZERO;
        }
        if (sumAmtReceipt == null) {
            sumAmtReceipt = BigDecimal.ZERO;
        }
        if ("B".equals(header.getPayWay())) {
            payFlowCashMapper.updateAmtReceipt(header.getBankDoc(), sumAmtProcess, sumAmtReceipt);
        } else {
            payFlowBillMapper.updateAmtReceipt(header.getBankDoc(), sumAmtProcess, sumAmtReceipt);
        }
    }

    /**
     * 业务数据校验
     */
    private void checkData(PayReceParamVO payReceParamVO) {
        PayReceDataVO data = payReceParamVO.getData();
        PayReceiptHeader header = data.getHeader();
        // 工作流按钮权限检查
        ClientResult clientResult = clientCore.wf.wfCheckBtn(header.getBussId(), ClientSecurity.getUserId(), "U", payReceParamVO.getWfEvt().getOperate());
        if (clientResult != null && "E".equals(clientResult.getStatus())) {
            throw new ClientCustomException(clientResult.getMsg());
        }
        // 校验认领金额不超过未认领金额
        if ("B".equals(header.getPayWay())) {
            PayFlowCash flow = payFlowCashMapper.selectById(header.getBankDoc());
            if (flow != null && flow.getAmt().subtract(flow.getAmtReceipt()).subtract(header.getAmtReceipt()).compareTo(BigDecimal.ZERO) < 0) {
                throw new ClientCustomException("当前银行流水的未认领金额不足");
            }
        } else {
            PayFlowBill bill = payFlowBillMapper.selectById(header.getBankDoc());
            if (bill != null && bill.getAmt().subtract(bill.getAmtReceipt()).subtract(header.getAmtReceipt()).compareTo(BigDecimal.ZERO) < 0) {
                throw new ClientCustomException("当前票据的未认领金额不足");
            }
        }
    }
}

package com.szd.z000.web.sap.sd.payRece.service;

import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.util.ClientUtilDate;
import com.szd.z000.base.orm.domain.sd.payRece.PayReceiptHeader;
import com.szd.z000.base.orm.domain.sd.payRece.PayReceiptItem;
import com.szd.z000.base.orm.domain.mdm.bankAcc.BankAcc;
import com.szd.z000.base.orm.domain.sd.payFlowBill.PayFlowBill;
import com.szd.z000.base.orm.domain.sd.payFlowCash.PayFlowCash;
import com.szd.z000.base.orm.mapper.mdm.bankAcc.BankAccMapper;
import com.szd.z000.base.orm.mapper.sd.payFlowBill.PayFlowBillMapper;
import com.szd.z000.base.orm.mapper.sd.payFlowCash.PayFlowCashMapper;
import com.szd.z000.base.orm.mapper.sd.payRece.PayReceiptHeaderMapper;
import com.szd.z000.base.orm.mapper.sd.payRece.PayReceiptItemMapper;
import com.szd.z000.common.sap.domain.payRece.SapPayReceiptCtr;
import com.szd.z000.common.sap.domain.payRece.SapPayReceiptCtrHeader;
import com.szd.z000.common.sap.domain.payRece.SapPayReceiptCtrItem;
import com.szd.z000.common.sap.domain.payRece.SapPayReceiptCtrItem2;
import com.szd.z000.common.sap.domain.service.SapPayReceiptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 回款认领SAP接口服务
 */
@Service
@Slf4j
public class PayReceiptSapService {

    @Resource
    private PayReceiptHeaderMapper payReceiptHeaderMapper;
    @Resource
    private PayReceiptItemMapper payReceiptItemMapper;
    @Resource
    private BankAccMapper bankAccMapper;
    @Resource
    private PayFlowCashMapper payFlowCashMapper;
    @Resource
    private PayFlowBillMapper payFlowBillMapper;
    @Resource
    private SapPayReceiptService sapPayReceiptService;

    /**
     * 同步SAP回款认领单
     *
     * @param bussId 回款认领单ID
     * @return SAP处理结果
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClientResult createSapPayReceipt(String bussId) {
        ClientResult clientResult = new ClientResult();
        // 查询回款认领头数据
        PayReceiptHeader header = payReceiptHeaderMapper.selectById(bussId);
        if (header == null || !"D".equals(header.getBussStatus())) {
            return clientResult;
        }
        // 查询行项目
        List<PayReceiptItem> itemList = payReceiptItemMapper.selectList(bussId);
        // 构建SAP请求
        SapPayReceiptCtr sapPayReceiptCtr = buildSapRequest(header, itemList);
        // 调用SAP接口
        clientResult = sapPayReceiptService.createPayReceipt(sapPayReceiptCtr, bussId);
        clientResult.setStatus(clientResult.getStatus());
        return clientResult;
    }

    /**
     * 冲销SAP回款认领单
     *
     * @param bussId 回款认领单ID
     * @return SAP处理结果
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClientResult reverseSapPayReceipt(String bussId) {
        ClientResult clientResult = new ClientResult();
        // 查询回款认领头数据
        PayReceiptHeader header = payReceiptHeaderMapper.selectById(bussId);
        if (header == null || header.getVchNum() == null || header.getVchNum().isEmpty()) {
            return clientResult;
        }
        // 查询行项目
        List<PayReceiptItem> itemList = payReceiptItemMapper.selectList(bussId);
        // 构建SAP请求（ZTYPE=R）
        SapPayReceiptCtr sapPayReceiptCtr = buildReverseSapRequest(header, itemList);
        // 调用SAP冲销接口
        clientResult = sapPayReceiptService.reversePayReceipt(sapPayReceiptCtr, bussId);
        clientResult.setStatus(clientResult.getStatus());
        return clientResult;
    }

    /**
     * 构建SAP冲销请求对象（ZTYPE=R）
     */
    private SapPayReceiptCtr buildReverseSapRequest(PayReceiptHeader header, List<PayReceiptItem> itemList) {
        SapPayReceiptCtr ctr = buildSapRequest(header, itemList);
        // 冲销时ZTYPE改为R
        ctr.getHeader().setZtype("R");
        return ctr;
    }

    /**
     * 构建SAP请求对象
     */
    private SapPayReceiptCtr buildSapRequest(PayReceiptHeader header, List<PayReceiptItem> itemList) {
        SapPayReceiptCtr ctr = new SapPayReceiptCtr();
        // 构建抬头
        SapPayReceiptCtrHeader ctrHeader = new SapPayReceiptCtrHeader();
        // HEADER（抬头）通用字段
        ctrHeader.setIfDocno(header.getBussDocId());
        ctrHeader.setZtype("C");
        ctrHeader.setBukrs(header.getCmpy());
        ctrHeader.setWaers(header.getCurr());
        ctrHeader.setBlart("SA");
        // 过账日期格式yyyyMMdd
        if (header.getBussDate() != null) {
            ctrHeader.setBudat(ClientUtilDate.getDate("yyyyMMdd", header.getBussDate()));
        }
        ctrHeader.setBktxt(header.getText());
        // 凭证货币金额
        String wrbtr = null;
        if (header.getAmtReceipt() != null) {
            wrbtr = header.getAmtReceipt().toString();
        }
        // 根据支付方式传递不同的HEADER字段
        if ("B".equals(header.getPayWay())) {
            // 电汇：查询电汇回款数据获取本方账户 → 查会计科目
            PayFlowCash payFlowCash = payFlowCashMapper.selectById(header.getBankDoc());
            String accNo = payFlowCash != null ? payFlowCash.getMyAccNo() : null;
            // 收款电汇字段
            ctrHeader.setHkont(getHkontByAccNo(accNo));
            ctrHeader.setSgtxt(header.getText());
            ctrHeader.setZuonr(header.getBussDocId());
            ctrHeader.setWrbtr(wrbtr);
        } else {
            // 票据：查询票据数据获取持票账户及其他票据信息
            PayFlowBill payFlowBill = payFlowBillMapper.selectById(header.getBankDoc());
            String accNo = payFlowBill != null ? payFlowBill.getHolderAcc() : null;
            // 收款票据信息字段（A_前缀
            ctrHeader.setAHkont(getHkontByAccNo(accNo));
            ctrHeader.setASgtxt(header.getText());
            ctrHeader.setAZuonr(header.getBussDocId());
            ctrHeader.setAWrbtr(wrbtr);
            ctrHeader.setAKunnr(header.getCust());
            ctrHeader.setAUmskz("W");
            // 票据独有字段
            if (payFlowBill != null) {
                ctrHeader.setAZfbdt(ClientUtilDate.getDate("yyyyMMdd", payFlowBill.getDueDate()));
                ctrHeader.setAWname(payFlowBill.getDrawer());
                ctrHeader.setAWbzog(payFlowBill.getPayee());
                ctrHeader.setAWdate(ClientUtilDate.getDate("yyyyMMdd", payFlowBill.getSignDate()));
            }
        }
        ctr.setHeader(ctrHeader);
        // 根据回款性质构建行项目
        String payNature = header.getPayNature();
        if ("10".equals(payNature) || "30".equals(payNature)) {
            // 收款并清账：传递ITEMS（未清项）
            ctr.setItems(buildItems(itemList));
        } else if ("20".equals(payNature) || "40".equals(payNature)) {
            // 预收款：传递ITEMS2（客户记账信息，取header字段，仅一条）
            ctr.setItems2(buildItems2(header));
        }
        return ctr;
    }

    /**
     * 通过账户查询本方账户主数据获取会计科目
     *
     * @param accNo 银行账户
     * @return 会计科目
     */
    private String getHkontByAccNo(String accNo) {
        if (accNo == null || accNo.isEmpty()) {
            return null;
        }
        BankAcc bankAcc = bankAccMapper.selectById(accNo);
        if (bankAcc != null) {
            return bankAcc.getAccs();
        }
        return null;
    }

    /**
     * 构建未清项列表（回款性质10/30）
     */
    private List<SapPayReceiptCtrItem> buildItems(List<PayReceiptItem> itemList) {
        if (CollectionUtils.isEmpty(itemList)) {
            return null;
        }
        List<SapPayReceiptCtrItem> sapItems = new ArrayList<>();
        for (PayReceiptItem item : itemList) {
            SapPayReceiptCtrItem sapItem = new SapPayReceiptCtrItem();
            sapItem.setBelnr(item.getVchNum());
            sapItem.setGjahr(item.getVchYear());
            sapItem.setBuzei(item.getVchNumItem());
            if (item.getClearAmt() != null) {
                sapItem.setWrbtr(item.getClearAmt().toString());
            }
            sapItems.add(sapItem);
        }
        return sapItems;
    }

    /**
     * 构建客户记账信息（回款性质20/40，仅一条，值取自header）
     */
    private List<SapPayReceiptCtrItem2> buildItems2(PayReceiptHeader header) {
        List<SapPayReceiptCtrItem2> sapItems2 = new ArrayList<>();
        SapPayReceiptCtrItem2 sapItem2 = new SapPayReceiptCtrItem2();
        sapItem2.setDfKunnr(header.getCust());
        sapItem2.setDfSgtxt(header.getText());
        if (header.getAmtReceipt() != null) {
            sapItem2.setDfWrbtr(header.getAmtReceipt().toString());
        }
        sapItems2.add(sapItem2);
        return sapItems2;
    }
}

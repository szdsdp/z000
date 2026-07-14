package com.szd.z000.web.sap.mm.invCheck.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientResultMsgBox;
import com.szd.core.client.util.ClientUtilDate;
import com.szd.z000.base.orm.domain.mm.invCheck.InvCheckHeader;
import com.szd.z000.base.orm.domain.mm.invCheck.InvCheckItem;
import com.szd.z000.common.sap.domain.invCheck.*;
import com.szd.z000.common.sap.domain.service.SapInvCheckCreateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 发票校验-SAP服务
 */
@Service
public class InvCheckSapService {
    private static final Logger logger = LoggerFactory.getLogger(InvCheckSapService.class);

    @Resource
    private InvCheckHeaderService invCheckHeaderService;
    @Resource
    private InvCheckItemService invCheckItemService;
    @Resource
    private SapInvCheckCreateService sapInvCheckCreateService;
    @Resource
    private ClientCore clientCore;

    /**
     * 推送SAP创建发票校验(正向)
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClientResult genSapInvCheck(String invCheckId) {
        ClientResult clientResult = new ClientResult();
        InvCheckHeader header = invCheckHeaderService.selectById(invCheckId);
        if (header == null || !"D".equals(header.getBussStatus())) return clientResult;
        SapInvCheckCtr sapCtr = buildSapCtr(invCheckId, false);
        clientResult = sapInvCheckCreateService.callInvCheck(sapCtr, invCheckId, false);
        return clientResult;
    }

    /**
     * 发票校验-SAP冲销（独立冲销按钮调用）
     * 与正向使用相同的数据结构，仅IF_RFLAG传X
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClientResult reverseSapInvCheck(String invCheckId) {
        ClientResult clientResult = new ClientResult();
        InvCheckHeader header = invCheckHeaderService.selectObjById(invCheckId);
        if (header == null || header.getReBelnr() == null || header.getReBelnr().isEmpty()) {
            clientResult.setStatus("E");
            clientResult.setMsg("未找到发票凭证，无法冲销");
            ClientResultMsgBox msgBox = new ClientResultMsgBox("E", "未找到发票凭证，无法冲销");
            clientResult.getRetMsgBox().add(msgBox);
            return clientResult;
        }
        SapInvCheckCtr sapCtr = buildSapCtr(invCheckId, true);
        clientResult = sapInvCheckCreateService.callInvCheck(sapCtr, invCheckId, true);
        return clientResult;
    }

    /**
     * 构建SAP请求数据
     *
     * @param invCheckId    单据ID
     * @param isReverse true=冲销(IF_RFLAG=X)，false=正向(IF_RFLAG为空)
     */
    private SapInvCheckCtr buildSapCtr(String invCheckId, boolean isReverse) {
        InvCheckHeader header = invCheckHeaderService.selectObjById(invCheckId);
        List<InvCheckItem> itemList = invCheckItemService.selectList(invCheckId);

        // 抬头
        SapInvCheckCtrHeader sapHeader = new SapInvCheckCtrHeader();
        sapHeader.setIfDocno(header.getBussDocId());
        sapHeader.setIfRflag(isReverse ? "X" : "");
        sapHeader.setInvoiceInd("X"); // 记帐发票写死X
        sapHeader.setDocType("RE"); // 凭证类型写死RE
        sapHeader.setDocDate(ClientUtilDate.dateToSap(header.getVchDate()));
        sapHeader.setPstngDate(ClientUtilDate.dateToSap(header.getPostDate()));
        sapHeader.setCompCode(header.getCmpy());
        sapHeader.setCurrency(header.getCurr());
        sapHeader.setGrossAmount(header.getInvAmt() != null ? header.getInvAmt().toString() : "");
        sapHeader.setPmnttrms(header.getTerms());
        sapHeader.setBlineDate(ClientUtilDate.dateToSap(header.getBlineDate()));
        sapHeader.setHeaderTxt(header.getTitle());
        sapHeader.setPymtMeth(header.getPayWay());

        // 行项目
        List<SapInvCheckCtrItem> sapItemList = new ArrayList<>();
        for (InvCheckItem item : itemList) {
            SapInvCheckCtrItem si = new SapInvCheckCtrItem();
            si.setInvoiceDocItem(String.format("%05d", item.getItemNo())); // itemNo转5位凭证项目号
            si.setPoNumber(item.getOrderNo());
            si.setPoItem(item.getOrderItemNo());
            si.setTaxCode(item.getTaxCode());
            si.setItemAmount(item.getAmt() != null ? item.getAmt().toString() : "");
            si.setTaxAmount(item.getTaxAmount() != null ? item.getTaxAmount().toString() : "");
            si.setQuantity(item.getQuantity() != null ? item.getQuantity().toString() : "");
            si.setPoUnit(item.getUnit());
            si.setItemText(item.getItemText());
            sapItemList.add(si);
        }

        SapInvCheckCtr sapCtr = new SapInvCheckCtr();
        sapCtr.setHeader(sapHeader);
        sapCtr.setItemList(sapItemList);
        return sapCtr;
    }
}

package com.szd.z000.web.sap.pp.orderReport.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.util.ClientUtilDate;
import com.szd.z000.base.orm.domain.pp.orderReport.OrderReportHeader;
import com.szd.z000.base.orm.domain.pp.orderReport.OrderReportItem;
import com.szd.z000.common.sap.domain.orderReport.*;
import com.szd.z000.common.sap.domain.service.SapOrderReportCreateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 生产订单报工-SAP服务
 */
@Service
public class OrderReportSapService {
    private static final Logger logger = LoggerFactory.getLogger(OrderReportSapService.class);

    @Resource
    private OrderReportHeaderService orderReportHeaderService;
    @Resource
    private OrderReportItemService orderReportItemService;
    @Resource
    private SapOrderReportCreateService sapOrderReportCreateService;
    @Resource
    private ClientCore clientCore;

    /**
     * 推送SAP创建生产订单报工
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClientResult genSapOrderReport(String reportId) {
        ClientResult clientResult = new ClientResult();
        OrderReportHeader header = orderReportHeaderService.selectById(reportId);
        if (header == null || !"D".equals(header.getBussStatus())) {
            return clientResult;
        }
        List<OrderReportItem> itemList = orderReportItemService.selectList(reportId);

        // 封装SAP参数-抬头
        SapOrderReportCtrHeader sapHeader = new SapOrderReportCtrHeader();
        sapHeader.setIfDocno(header.getBussDocId());
        sapHeader.setZtype("A");
        sapHeader.setAufnr(header.getAufnr());
        sapHeader.setConfno(header.getConfno());
        sapHeader.setConfcnt(header.getConfcnt());
        sapHeader.setBudat(ClientUtilDate.dateToSap(header.getBudat()));

        // 封装SAP参数-行项目
        List<SapOrderReportCtrItem> itemListSap = new ArrayList<>();
        for (OrderReportItem item : itemList) {
            SapOrderReportCtrItem sapItem = new SapOrderReportCtrItem();
            sapItem.setOperation(item.getOperation());
            sapItem.setConfText(item.getConfText());
            sapItem.setYield(item.getYield());
            sapItem.setScrap(item.getScrap());
            sapItem.setRework(item.getRework());
            itemListSap.add(sapItem);
        }

        // 组装SAP参数
        SapOrderReportCtr sapOrderReportCtr = new SapOrderReportCtr();
        sapOrderReportCtr.setHeader(sapHeader);
        sapOrderReportCtr.setItemList(itemListSap);

        // 调用SAP接口
        clientResult = sapOrderReportCreateService.createOrderReport(sapOrderReportCtr, reportId, false);

        return clientResult;
    }

    /**
     * 推送SAP冲销生产订单报工
     */
    public ClientResult reverseSapOrderReport(String reportId) {
        ClientResult clientResult = new ClientResult();
        OrderReportHeader header = orderReportHeaderService.selectById(reportId);
        if (header == null || !"F".equals(header.getBussStatus())) {
            clientResult.setStatus("E");
            clientResult.setMsg("仅完成状态的报工单才能冲销");
            return clientResult;
        }
        if (header.getConfno() == null || header.getConfno().isEmpty()) {
            clientResult.setStatus("E");
            clientResult.setMsg("报工单未生成确认编号，无法冲销");
            return clientResult;
        }
        if (header.getRevConfcnt() != null && !header.getRevConfcnt().isEmpty()) {
            clientResult.setStatus("E");
            clientResult.setMsg("报工单已冲销，无需重复冲销");
            return clientResult;
        }

        // 封装SAP参数，ztype=B
        SapOrderReportCtrHeader sapHeader = new SapOrderReportCtrHeader();
        sapHeader.setIfDocno(header.getBussDocId());
        sapHeader.setZtype("B");
        sapHeader.setAufnr(header.getAufnr());
        sapHeader.setConfno(header.getConfno());
        sapHeader.setConfcnt(header.getConfcnt());
        sapHeader.setBudat(ClientUtilDate.dateToSap(header.getBudat()));

        SapOrderReportCtr sapOrderReportCtr = new SapOrderReportCtr();
        sapOrderReportCtr.setHeader(sapHeader);
        sapOrderReportCtr.setItemList(new ArrayList<>());

        return sapOrderReportCreateService.createOrderReport(sapOrderReportCtr, reportId, true);
    }
}

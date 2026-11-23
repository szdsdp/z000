package com.szd.z000.web.sap.pp.orderCreate.service;

import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.util.ClientUtilDate;
import com.szd.z000.base.orm.domain.pp.orderCreate.OrderCreateHeader;
import com.szd.z000.base.orm.mapper.pp.orderCreate.OrderCreateHeaderMapper;
import com.szd.z000.common.sap.domain.orderCreate.SapOrderCreateCtr;
import com.szd.z000.common.sap.domain.orderCreate.SapOrderCreateCtrHeader;
import com.szd.z000.common.sap.domain.orderCreate.SapOrderCreateReleaseCtr;
import com.szd.z000.common.sap.domain.orderCreate.SapOrderCreateReleaseCtrHeader;
import com.szd.z000.common.sap.domain.orderCreate.SapOrderCreateCloseCtr;
import com.szd.z000.common.sap.domain.orderCreate.SapOrderCreateCloseCtrHeader;
import com.szd.z000.common.sap.domain.service.SapOrderCreateCreateService;
import com.szd.z000.common.sap.domain.service.SapOrderCreateReleaseService;
import com.szd.z000.common.sap.domain.service.SapOrderCreateCloseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 生产订单创建-SAP服务
 */
@Service
public class OrderCreateSapService {
    private static final Logger logger = LoggerFactory.getLogger(OrderCreateSapService.class);

    @Resource
    private OrderCreateHeaderService orderCreateHeaderService;
    @Resource
    private SapOrderCreateCreateService sapOrderCreateCreateService;
    @Resource
    private SapOrderCreateReleaseService sapOrderCreateReleaseService;
    @Resource
    private SapOrderCreateCloseService sapOrderCreateCloseService;
    @Resource
    private OrderCreateHeaderMapper orderCreateHeaderMapper;

    /**
     * 推送SAP创建生产订单
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClientResult genSapOrderCreate(String applyId) {
        ClientResult clientResult = new ClientResult();
        OrderCreateHeader header = orderCreateHeaderService.selectById(applyId);
        if (header == null || !"D".equals(header.getBussStatus())) {
            return clientResult;
        }

        // 封装SAP参数-抬头
        SapOrderCreateCtrHeader sapHeader = new SapOrderCreateCtrHeader();
        sapHeader.setIfDocno(header.getBussDocId());
        sapHeader.setMaterial(header.getMtlCode());
        sapHeader.setPlant(header.getFact());
        sapHeader.setOrderType(header.getOrderType());
        sapHeader.setBasicStartDate(ClientUtilDate.dateToSap(header.getStartDate()));
        sapHeader.setBasicEndDate(ClientUtilDate.dateToSap(header.getEndDate()));
        if (header.getQuantity() != null) {
            sapHeader.setQuantity(header.getQuantity().toPlainString());
        }
        sapHeader.setQuantityUom(header.getUnit());

        // 组装SAP参数
        SapOrderCreateCtr sapOrderCreateCtr = new SapOrderCreateCtr();
        sapOrderCreateCtr.setHeader(sapHeader);

        // 调用SAP接口创建生产订单
        clientResult = sapOrderCreateCreateService.createOrderCreate(sapOrderCreateCtr, applyId);

        return clientResult;
    }

    /**
     * 下达生产订单
     */
    public ClientResult releaseOrderCreate(String applyId) {
        ClientResult clientResult = new ClientResult();
        OrderCreateHeader header = orderCreateHeaderService.selectById(applyId);
        if (header == null || !"F".equals(header.getBussStatus())) {
            clientResult.setStatus("E");
            clientResult.setMsg("仅完成状态的生产订单才能下达");
            return clientResult;
        }
        if (header.getAufnr() == null || header.getAufnr().isEmpty()) {
            clientResult.setStatus("E");
            clientResult.setMsg("生产订单未生成订单号，无法下达");
            return clientResult;
        }
        if ("1".equals(header.getReleased())) {
            clientResult.setStatus("E");
            clientResult.setMsg("生产订单已下达，无需重复下达");
            return clientResult;
        }

        // 封装SAP参数
        SapOrderCreateReleaseCtr sapCtr = new SapOrderCreateReleaseCtr();
        SapOrderCreateReleaseCtrHeader sapHeader = new SapOrderCreateReleaseCtrHeader();
        sapHeader.setAufnr(header.getAufnr());
        sapCtr.setHeader(sapHeader);

        // 调用SAP下达
        clientResult = sapOrderCreateReleaseService.release(sapCtr, applyId);

        // 更新下达状态
        String released = "S".equals(clientResult.getStatus()) ? "1" : "0";
        orderCreateHeaderMapper.updateReleased(applyId, released);

        return clientResult;
    }

    /**
     * 关闭/取消关闭生产订单
     * @param applyId 申请单ID
     * @param ztype C技术关闭 / R取消关闭
     */
    public ClientResult closeOrderCreate(String applyId, String ztype) {
        ClientResult clientResult = new ClientResult();
        OrderCreateHeader header = orderCreateHeaderService.selectById(applyId);
        if (header == null || !"F".equals(header.getBussStatus())) {
            clientResult.setStatus("E");
            clientResult.setMsg("仅完成状态的生产订单才能操作");
            return clientResult;
        }
        if (header.getAufnr() == null || header.getAufnr().isEmpty()) {
            clientResult.setStatus("E");
            clientResult.setMsg("生产订单未生成订单号，无法操作");
            return clientResult;
        }
        if (!"1".equals(header.getReleased())) {
            clientResult.setStatus("E");
            clientResult.setMsg("生产订单未下达，无法操作");
            return clientResult;
        }
        // 技术关闭：已关闭则拦截；取消关闭：未关闭则拦截
        if ("C".equals(ztype) && "1".equals(header.getClosed())) {
            clientResult.setStatus("E");
            clientResult.setMsg("生产订单已关闭，无需重复关闭");
            return clientResult;
        }
        if ("R".equals(ztype) && !"1".equals(header.getClosed())) {
            clientResult.setStatus("E");
            clientResult.setMsg("生产订单未关闭，无需取消关闭");
            return clientResult;
        }

        SapOrderCreateCloseCtr sapCtr = new SapOrderCreateCloseCtr();
        SapOrderCreateCloseCtrHeader sapHeader = new SapOrderCreateCloseCtrHeader();
        sapHeader.setAufnr(header.getAufnr());
        sapHeader.setZtype(ztype);
        sapCtr.setHeader(sapHeader);

        clientResult = sapOrderCreateCloseService.close(sapCtr);

        if ("S".equals(clientResult.getStatus())) {
            orderCreateHeaderMapper.updateClosed(applyId, "C".equals(ztype) ? "1" : "0");
        }

        return clientResult;
    }
}

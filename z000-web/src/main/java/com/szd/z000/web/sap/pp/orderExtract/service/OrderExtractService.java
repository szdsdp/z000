package com.szd.z000.web.sap.pp.orderExtract.service;

import com.szd.core.client.domain.common.ClientMdmReqCommon;
import com.szd.core.client.domain.common.ClientMdmReqCommonItem;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.util.ClientUtilDate;
import com.szd.z000.base.orm.domain.pp.orderExtract.OrderExtractHeader;
import com.szd.z000.base.orm.domain.pp.orderExtract.OrderExtractModule;
import com.szd.z000.base.orm.domain.pp.orderExtract.OrderExtractProcess;
import com.szd.z000.base.orm.mapper.pp.orderExtract.OrderExtractHeaderMapper;
import com.szd.z000.base.orm.mapper.pp.orderExtract.OrderExtractModuleMapper;
import com.szd.z000.base.orm.mapper.pp.orderExtract.OrderExtractProcessMapper;
import com.szd.z000.common.sap.domain.orderExtract.SapOrderExtractCtrItemAfko;
import com.szd.z000.common.sap.domain.orderExtract.SapOrderExtractCtrItemAfvcs;
import com.szd.z000.common.sap.domain.orderExtract.SapOrderExtractCtrItemResbs;
import com.szd.z000.common.sap.domain.orderExtract.SapOrderExtractCtrRet;
import com.szd.z000.common.sap.domain.orderExtract.SapOrderExtractCtrRetItem;
import com.szd.z000.common.sap.domain.orderExtract.SapOrderExtractCtrRetItems;
import com.szd.z000.common.sap.domain.service.SapOrderExtractSyncService;
import com.szd.z000.web.sap.pp.orderExtract.domain.OrderExtractDataVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 生产订单抽取-服务类
 */
@Service
public class OrderExtractService {
    private static final Logger logger = LoggerFactory.getLogger(OrderExtractService.class);

    @Resource
    private OrderExtractHeaderMapper orderExtractHeaderMapper;
    @Resource
    private OrderExtractProcessMapper orderExtractProcessMapper;
    @Resource
    private OrderExtractModuleMapper orderExtractModuleMapper;
    @Resource
    private SapOrderExtractSyncService sapOrderExtractSyncService;

    public List<OrderExtractHeader> selectList(ClientRpParam param) {
        return orderExtractHeaderMapper.selectList(param);
    }

    /**
     * 查询订单详情（含工序和组件）
     */
    public OrderExtractDataVO selectData(String orderNo) {
        OrderExtractDataVO dataVO = new OrderExtractDataVO();
        dataVO.setHeader(orderExtractHeaderMapper.selectById(orderNo));
        dataVO.setProcessList(orderExtractProcessMapper.selectByOrderNo(orderNo));
        dataVO.setModuleList(orderExtractModuleMapper.selectByOrderNo(orderNo));
        return dataVO;
    }

    /**
     * 从SAP抽取生产订单数据
     * 分页调用SAP接口，逐页解析并保存订单头、工序、组件数据
     *
     * @param params 查询参数(bdate/edate/bukrs/auart/werks)
     * @return 同步结果
     */
    public ClientResult sync(Map<String, String> params) {
        ClientResult result = new ClientResult();
        int totalCount = 0;
        int pageNo = 1;
        int pageSize = 100;

        // 分页循环调用SAP接口
        while (true) {
            // 构建SAP请求参数：查询条件 + 分页信息
            String bdate = params.get("bdate");
            String edate = params.get("edate");
            String bukrs = params.get("bukrs");
            String auart = params.get("auart");
            String werks = params.get("werks");

            ClientMdmReqCommon req = new ClientMdmReqCommon();
            List<ClientMdmReqCommonItem> itemsRanges = new ArrayList<>();

            // 设置分页参数
            ClientMdmReqCommonItem pageNoItem = new ClientMdmReqCommonItem();
            pageNoItem.setFilename("PAGENO");
            pageNoItem.setSign("I");
            pageNoItem.setOption("EQ");
            pageNoItem.setLow(String.valueOf(pageNo));
            itemsRanges.add(pageNoItem);

            ClientMdmReqCommonItem pageSizeItem = new ClientMdmReqCommonItem();
            pageSizeItem.setFilename("PAGESIZE");
            pageSizeItem.setSign("I");
            pageSizeItem.setOption("EQ");
            pageSizeItem.setLow(String.valueOf(pageSize));
            itemsRanges.add(pageSizeItem);

            // 设置日期范围条件（BEDAT + BT）
            if (bdate != null && !bdate.isEmpty()) {
                ClientMdmReqCommonItem bdateItem = new ClientMdmReqCommonItem();
                bdateItem.setFilename("BEDAT");
                bdateItem.setSign("I");
                bdateItem.setOption("BT");
                bdateItem.setLow(ClientUtilDate.strToSap(bdate));
                bdateItem.setHigh(ClientUtilDate.strToSap(edate));
                itemsRanges.add(bdateItem);
            }
            if (bukrs != null && !bukrs.isEmpty()) {
                ClientMdmReqCommonItem bukrsItem = new ClientMdmReqCommonItem();
                bukrsItem.setFilename("BUKRS");
                bukrsItem.setSign("I");
                bukrsItem.setOption("EQ");
                bukrsItem.setLow(bukrs);
                itemsRanges.add(bukrsItem);
            }
            if (auart != null && !auart.isEmpty()) {
                ClientMdmReqCommonItem auartItem = new ClientMdmReqCommonItem();
                auartItem.setFilename("AUART");
                auartItem.setSign("I");
                auartItem.setOption("EQ");
                auartItem.setLow(auart);
                itemsRanges.add(auartItem);
            }
            if (werks != null && !werks.isEmpty()) {
                ClientMdmReqCommonItem werksItem = new ClientMdmReqCommonItem();
                werksItem.setFilename("WERKS");
                werksItem.setSign("I");
                werksItem.setOption("EQ");
                werksItem.setLow(werks);
                itemsRanges.add(werksItem);
            }

            req.setItemsRanges(itemsRanges);

            // 调用SAP接口并校验返回结果
            SapOrderExtractCtrRet ret = sapOrderExtractSyncService.callSap(req);
            if (ret == null) {
                throw new ClientCustomException("SAP生产订单抽取接口调用失败");
            }
            if (ret.getHeader() == null || !"S".equals(ret.getHeader().getZstau())) {
                throw new ClientCustomException("SAP生产订单抽取失败：" +
                        (ret.getHeader() != null ? ret.getHeader().getZmess() : ""));
            }

            // 当前页无数据则退出循环
            SapOrderExtractCtrRetItems items = ret.getItems();
            if (items == null || CollectionUtils.isEmpty(items.getItemList())) {
                break;
            }

            // 遍历当前页每条订单数据
            for (SapOrderExtractCtrRetItem retItem : items.getItemList()) {
                // 获取AFKO订单头数据，跳过无效记录
                SapOrderExtractCtrItemAfko afko = retItem.getAfko();
                if (afko == null) {
                    continue;
                }
                String orderNo = afko.getAufnr();
                if (orderNo == null || orderNo.isEmpty()) {
                    continue;
                }

                // 解析AFKO订单头数据并保存（upsert）
                OrderExtractHeader h = new OrderExtractHeader();
                h.setOrderNo(orderNo);
                h.setOrderType(afko.getAuart());
                h.setOrderDesc(afko.getKtext());
                h.setProdVer(afko.getKostv());
                h.setFinishDate(ClientUtilDate.sapToDate(afko.getGltrp()));
                h.setStartDate(ClientUtilDate.sapToDate(afko.getGstrp()));
                h.setResNo(afko.getRsnum());
                h.setShippedQty(parseBigDecimal(afko.getGasmg()));
                h.setOrderQty(parseBigDecimal(afko.getGamng()));
                h.setUnit(afko.getGmein());
                h.setMtlCode(afko.getMatnr());
                h.setFact(afko.getWerks());
                h.setRoutingNo(afko.getAufpl());
                orderExtractHeaderMapper.batchUpsert(java.util.Collections.singletonList(h));
                totalCount++;

                // 处理工序数据：先删后插，按SAP返回顺序生成行号
                orderExtractProcessMapper.deleteByOrderNo(orderNo);
                List<SapOrderExtractCtrItemAfvcs> afvcsList = retItem.getAfvcsList();
                if (!CollectionUtils.isEmpty(afvcsList)) {
                    List<OrderExtractProcess> processList = new ArrayList<>();
                    int itemNo = 0;
                    for (SapOrderExtractCtrItemAfvcs afvcs : afvcsList) {
                        OrderExtractProcess p = new OrderExtractProcess();
                        p.setOrderNo(orderNo);
                        p.setItemNo(++itemNo);
                        p.setRoutingNo(afvcs.getAufpl());
                        p.setOperInnerNo(afvcs.getAplzl());
                        p.setOperNo(afvcs.getVornr());
                        p.setStdTextCode(afvcs.getKtsch());
                        p.setOperDesc(afvcs.getLtxa1());
                        processList.add(p);
                    }
                    if (!processList.isEmpty()) {
                        orderExtractProcessMapper.batchInsert(processList);
                    }
                }

                // 处理组件数据：先删后插，按SAP返回顺序生成行号
                orderExtractModuleMapper.deleteByOrderNo(orderNo);
                List<SapOrderExtractCtrItemResbs> resbsList = retItem.getResbsList();
                if (!CollectionUtils.isEmpty(resbsList)) {
                    List<OrderExtractModule> moduleList = new ArrayList<>();
                    int itemNo = 0;
                    for (SapOrderExtractCtrItemResbs resbs : resbsList) {
                        OrderExtractModule m = new OrderExtractModule();
                        m.setOrderNo(orderNo);
                        m.setItemNo(++itemNo);
                        m.setResNo(resbs.getRsnum());
                        m.setResItem(resbs.getRspos());
                        m.setMtlCode(resbs.getMatnr());
                        m.setFact(resbs.getWerks());
                        m.setStore(resbs.getLgort());
                        m.setReqDate(resbs.getBdter());
                        m.setReqQty(resbs.getBdmng());
                        m.setUnit(resbs.getMeins());
                        moduleList.add(m);
                    }
                    if (!moduleList.isEmpty()) {
                        orderExtractModuleMapper.batchInsert(moduleList);
                    }
                }

                logger.info("生产订单抽取第{}页，订单号:{}", pageNo, orderNo);
            }
            // 翻页继续
            pageNo++;
        }

        result.setStatus("S");
        result.setMsg("抽取完成，共处理" + totalCount + "条");
        return result;
    }

    /**
     * 解析字符串为BigDecimal
     */
    private BigDecimal parseBigDecimal(String numStr) {
        if (numStr == null || numStr.isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(numStr);
        } catch (Exception e) {
            logger.warn("解析数值失败: {}", numStr);
            return null;
        }
    }
}

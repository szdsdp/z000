package com.szd.z000.web.sap.mm.orderSearch.service;

import com.szd.core.client.domain.common.ClientMdmReqCommon;
import com.szd.core.client.domain.common.ClientMdmReqCommonItem;
import com.szd.core.client.domain.common.ClientPageTableRet;
import com.szd.core.client.domain.common.ClientRangeQuery;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.z000.common.sap.domain.orderSearch.SapOrderSearchCtrRet;
import com.szd.z000.common.sap.domain.orderSearch.SapOrderSearchCtrRetItem;
import com.szd.z000.common.sap.domain.service.SapOrderSearchService;
import com.szd.z000.web.common.util.SessionCacheUtil;
import com.szd.z000.web.sap.mm.orderSearch.domain.OrderSearchResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 采购订单抽取-服务类
 */
@Service
public class OrderSearchService {
    private static final Logger logger = LoggerFactory.getLogger(OrderSearchService.class);

    @Resource
    private SapOrderSearchService sapOrderSearchService;

    /**
     * SAP分页查询每页大小
     */
    private static final int SAP_PAGE_SIZE = 100;

    /**
     * session缓存命名空间
     */
    private static final String CACHE_NAMESPACE = "orderSearch";

    /**
     * 查询采购订单列表（含分页）
     */
    public ClientPageTableRet queryList(ClientRpParam param) {
        // 提取查询条件
        Map<String, List<ClientRangeQuery>> ranges = param.getRanges();
        String bukrs = extractRangeValue(ranges, "bukrs");
        String lifnr = extractRangeValue(ranges, "lifnr");
        String ebeln = extractRangeValue(ranges, "ebeln");
        String bsart = extractRangeValue(ranges, "bsart");
        String ekorg = extractRangeValue(ranges, "ekorg");
        String ekgrp = extractRangeValue(ranges, "ekgrp");

        // 提取分页参数
        int pageNum = param.getPageNum() != null && param.getPageNum() > 0 ? param.getPageNum() : 1;
        int pageSize = param.getPageSize() != null && param.getPageSize() > 0 ? param.getPageSize() : 20;

        logger.info("采购订单抽取查询 - bukrs: {}, lifnr: {}, ebeln: {}, pageNum: {}, pageSize: {}",
                bukrs, lifnr, ebeln, pageNum, pageSize);

        // 计算查询条件指纹
        String cacheKey = buildCacheKey(bukrs, lifnr, ebeln, bsart, ekorg, ekgrp);

        // 通过session缓存获取数据：条件不变则复用，条件变化则自动重新获取
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        List<OrderSearchResultVO> allItems = SessionCacheUtil.getOrFetch(
                session, CACHE_NAMESPACE, cacheKey,
                () -> querySapData(bukrs, lifnr, ebeln, bsart, ekorg, ekgrp)
        );

        // 无数据返回空结果
        if (CollectionUtils.isEmpty(allItems)) {
            return createEmptyResult();
        }

        // 内存分页
        int total = allItems.size();
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, total);

        if (start >= total) {
            start = 0;
            end = Math.min(pageSize, total);
        }

        List<OrderSearchResultVO> pagedItems = allItems.subList(start, end);

        // 构建分页返回结果
        ClientPageTableRet result = new ClientPageTableRet();
        result.setCode(200);
        result.setMsg("查询成功");
        result.setRows(new ArrayList<>(pagedItems));
        result.setTotal(total);

        logger.info("返回分页数据 - 当前页: {}, 每页大小: {}, 总条数: {}", pageNum, pageSize, total);

        return result;
    }

    /**
     * 调用SAP查询采购订单数据（分页循环，直至无数据返回）
     */
    private List<OrderSearchResultVO> querySapData(String bukrs, String lifnr,
                                                    String ebeln, String bsart, String ekorg, String ekgrp) {
        int pageNo = 1;
        List<OrderSearchResultVO> resultList = new ArrayList<>();

        // 循环分页调用SAP，直至无数据返回
        while (true) {
            // 构建SAP请求
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
            pageSizeItem.setLow(String.valueOf(SAP_PAGE_SIZE));
            itemsRanges.add(pageSizeItem);

            // 设置查询条件
            addRangeItem(itemsRanges, "BUKRS", bukrs);
            addRangeItem(itemsRanges, "LIFNR", lifnr);
            addRangeItem(itemsRanges, "EBELN", ebeln);
            addRangeItem(itemsRanges, "BSART", bsart);
            addRangeItem(itemsRanges, "EKORG", ekorg);
            addRangeItem(itemsRanges, "EKGRP", ekgrp);

            req.setItemsRanges(itemsRanges);

            // 调用SAP接口
            SapOrderSearchCtrRet ret = sapOrderSearchService.callSap(req);
            if (ret == null) {
                throw new ClientCustomException("SAP采购订单抽取接口调用失败");
            }
            if (ret.getHeader() == null || !"S".equals(ret.getHeader().getZstau())) {
                throw new ClientCustomException("SAP采购订单抽取查询失败：" +
                        (ret.getHeader() != null ? ret.getHeader().getZmess() : ""));
            }

            // 无数据则退出循环
            if (CollectionUtils.isEmpty(ret.getItems())) {
                break;
            }

            // 转换为前端展示格式
            for (SapOrderSearchCtrRetItem item : ret.getItems()) {
                OrderSearchResultVO vo = new OrderSearchResultVO();
                BeanUtils.copyProperties(item, vo);
                resultList.add(vo);
            }

            pageNo++;
        }

        return resultList;
    }

    /**
     * 添加查询条件
     */
    private void addRangeItem(List<ClientMdmReqCommonItem> itemsRanges, String filename, String value) {
        if (value != null && !value.isEmpty()) {
            ClientMdmReqCommonItem item = new ClientMdmReqCommonItem();
            item.setFilename(filename);
            item.setSign("I");
            item.setOption("EQ");
            item.setLow(value);
            itemsRanges.add(item);
        }
    }

    /**
     * 从ranges中提取查询值
     */
    private String extractRangeValue(Map<String, List<ClientRangeQuery>> ranges, String fieldName) {
        if (ranges == null) {
            return null;
        }
        List<ClientRangeQuery> queries = ranges.get(fieldName);
        if (!CollectionUtils.isEmpty(queries)) {
            String value = queries.get(0).getLow();
            if (value != null && !value.isEmpty()) {
                return value;
            }
        }
        return null;
    }

    /**
     * 创建空的分页结果
     */
    private ClientPageTableRet createEmptyResult() {
        ClientPageTableRet result = new ClientPageTableRet();
        result.setRows(Collections.emptyList());
        result.setTotal(0);
        return result;
    }

    /**
     * 构建查询条件指纹，用于判断两次查询条件是否一致
     */
    private String buildCacheKey(String bukrs, String lifnr, String ebeln,
                                  String bsart, String ekorg, String ekgrp) {
        return String.format("%s|%s|%s|%s|%s|%s",
                bukrs, lifnr, ebeln, bsart, ekorg, ekgrp);
    }
}

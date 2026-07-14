package com.szd.z000.web.sap.mm.storeSearch.service;

import com.szd.core.client.domain.common.ClientMdmReqCommon;
import com.szd.core.client.domain.common.ClientMdmReqCommonItem;
import com.szd.core.client.domain.common.ClientPageTableRet;
import com.szd.core.client.domain.common.ClientRangeQuery;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.util.ClientUtilDate;
import com.szd.z000.common.sap.domain.storeSearch.SapStoreSearchCtrRet;
import com.szd.z000.common.sap.domain.storeSearch.SapStoreSearchCtrRetItem;
import com.szd.z000.common.sap.domain.service.SapStoreSearchService;
import com.szd.z000.web.sap.mm.storeSearch.domain.StoreSearchResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 物料凭证抽取-服务类
 */
@Service
public class StoreSearchService {
    private static final Logger logger = LoggerFactory.getLogger(StoreSearchService.class);

    @Resource
    private SapStoreSearchService sapStoreSearchService;

    /**
     * SAP分页查询每页大小
     */
    private static final int SAP_PAGE_SIZE = 100;

    /**
     * 查询物料凭证列表（含分页）
     */
    public ClientPageTableRet queryList(ClientRpParam param) {
        // 提取查询条件
        Map<String, List<ClientRangeQuery>> ranges = param.getRanges();
        String bukrs = extractRangeValue(ranges, "bukrs");
        String lifnr = extractRangeValue(ranges, "lifnr");
        String waers = extractRangeValue(ranges, "waers");
        String ebeln = extractRangeValue(ranges, "ebeln");
        String matnr = extractRangeValue(ranges, "matnr");
        String bdate = extractRangeValue(ranges, "bdate");
        String edate = extractRangeValue(ranges, "edate");

        // 提取分页参数
        int pageNum = param.getPageNum() != null && param.getPageNum() > 0 ? param.getPageNum() : 1;
        int pageSize = param.getPageSize() != null && param.getPageSize() > 0 ? param.getPageSize() : 20;

        logger.info("物料凭证抽取查询 - bukrs: {}, lifnr: {}, ebeln: {}, pageNum: {}, pageSize: {}",
                bukrs, lifnr, ebeln, pageNum, pageSize);

        // 调用SAP查询全部物料凭证数据
        List<StoreSearchResultVO> allItems = querySapData(bukrs, lifnr, waers, ebeln, matnr, bdate, edate);

        logger.info("SAP返回 {} 条物料凭证记录", allItems.size());

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

        List<StoreSearchResultVO> pagedItems = allItems.subList(start, end);

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
     * 调用SAP查询物料凭证数据（分页循环，直至无数据返回）
     */
    private List<StoreSearchResultVO> querySapData(String bukrs, String lifnr, String waers,
                                                    String ebeln, String matnr, String bdate, String edate) {
        int pageNo = 1;
        List<StoreSearchResultVO> resultList = new ArrayList<>();

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
            addRangeItem(itemsRanges, "WAERS", waers);
            addRangeItem(itemsRanges, "EBELN", ebeln);
            addRangeItem(itemsRanges, "MATNR", matnr);
            if (bdate != null && !bdate.isEmpty()) {
                ClientMdmReqCommonItem bdateItem = new ClientMdmReqCommonItem();
                bdateItem.setFilename("BEDAT");
                bdateItem.setSign("I");
                bdateItem.setOption("BT");
                bdateItem.setLow(ClientUtilDate.strToSap(bdate));
                bdateItem.setHigh(ClientUtilDate.strToSap(edate));
                itemsRanges.add(bdateItem);
            }

            req.setItemsRanges(itemsRanges);

            // 调用SAP接口
            SapStoreSearchCtrRet ret = sapStoreSearchService.callSap(req);
            if (ret == null) {
                throw new ClientCustomException("SAP物料凭证抽取接口调用失败");
            }
            if (ret.getHeader() == null || !"S".equals(ret.getHeader().getZstau())) {
                throw new ClientCustomException("SAP物料凭证抽取查询失败：" +
                        (ret.getHeader() != null ? ret.getHeader().getZmess() : ""));
            }

            // 无数据则退出循环
            if (CollectionUtils.isEmpty(ret.getItems())) {
                break;
            }

            // 转换为前端展示格式
            for (SapStoreSearchCtrRetItem item : ret.getItems()) {
                StoreSearchResultVO vo = new StoreSearchResultVO();
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
}

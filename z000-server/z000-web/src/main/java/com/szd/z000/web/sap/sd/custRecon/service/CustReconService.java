package com.szd.z000.web.sap.sd.custRecon.service;

import com.szd.core.client.domain.common.ClientMdmReqCommon;
import com.szd.core.client.domain.common.ClientMdmReqCommonItem;
import com.szd.core.client.domain.common.ClientPageTableRet;
import com.szd.core.client.domain.common.ClientRangeQuery;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.util.ClientUtilDate;
import com.szd.z000.common.sap.domain.custRecon.SapCustReconCtrRet;
import com.szd.z000.common.sap.domain.custRecon.SapCustReconCtrRetItem;
import com.szd.z000.common.sap.domain.service.SapCustReconService;
import com.szd.z000.web.common.util.SessionCacheUtil;
import com.szd.z000.web.sap.sd.custRecon.domain.CustReconResultVO;
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
 * 客商对账查询-服务类
 */
@Service
public class CustReconService {
    private static final Logger logger = LoggerFactory.getLogger(CustReconService.class);

    @Resource
    private SapCustReconService sapCustReconService;

    /** SAP分页查询每页大小 */
    private static final int SAP_PAGE_SIZE = 100;

    /**
     * session缓存命名空间
     */
    private static final String CACHE_NAMESPACE = "custRecon";

    /**
     * 查询客商对账列表（含分页）
     */
    public ClientPageTableRet queryList(ClientRpParam param) {
        // 提取查询条件
        Map<String, List<ClientRangeQuery>> ranges = param.getRanges();
        String bdate = extractRangeValue(ranges, "bdate");
        String edate = extractRangeValue(ranges, "edate");
        String bukrs = extractRangeValue(ranges, "bukrs");
        String kunnr = extractRangeValue(ranges, "kunnr");

        // 提取分页参数
        int pageNum = param.getPageNum() != null && param.getPageNum() > 0 ? param.getPageNum() : 1;
        int pageSize = param.getPageSize() != null && param.getPageSize() > 0 ? param.getPageSize() : 20;

        logger.info("客商对账查询 - bdate: {}, edate: {}, bukrs: {}, kunnr: {}, pageNum: {}, pageSize: {}",
                bdate, edate, bukrs, kunnr, pageNum, pageSize);

        // 计算查询条件指纹
        String cacheKey = buildCacheKey(bdate, edate, bukrs, kunnr);

        // 通过session缓存获取数据：条件不变则复用，条件变化则自动重新获取
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        List<CustReconResultVO> allItems = SessionCacheUtil.getOrFetch(
                session, CACHE_NAMESPACE, cacheKey,
                () -> querySapData(bdate, edate, bukrs, kunnr)
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

        List<CustReconResultVO> pagedItems = allItems.subList(start, end);

        // 构建分页返回结果
        ClientPageTableRet result = new ClientPageTableRet();
        result.setRows(new ArrayList<>(pagedItems));
        result.setTotal(total);

        logger.info("返回分页数据 - 当前页: {}, 每页大小: {}, 总条数: {}", pageNum, pageSize, total);

        return result;
    }

    /**
     * 调用SAP查询对账数据（分页循环，直至无数据返回）
     */
    private List<CustReconResultVO> querySapData(String bdate, String edate, String bukrs, String kunnr) {
        int pageNo = 1;
        List<CustReconResultVO> resultList = new ArrayList<>();

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
            if (bdate != null && !bdate.isEmpty()) {
                ClientMdmReqCommonItem bdateItem = new ClientMdmReqCommonItem();
                bdateItem.setFilename("ERDAT");
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
            if (kunnr != null && !kunnr.isEmpty()) {
                ClientMdmReqCommonItem kunnrItem = new ClientMdmReqCommonItem();
                kunnrItem.setFilename("KUNNR");
                kunnrItem.setSign("I");
                kunnrItem.setOption("EQ");
                kunnrItem.setLow(kunnr);
                itemsRanges.add(kunnrItem);
            }

            req.setItemsRanges(itemsRanges);

            // 调用SAP接口
            SapCustReconCtrRet ret = sapCustReconService.callSap(req);
            if (ret == null) {
                throw new ClientCustomException("SAP客商对账查询接口调用失败");
            }
            if (ret.getHeader() == null || !"S".equals(ret.getHeader().getZstau())) {
                throw new ClientCustomException("SAP客商对账查询失败：" +
                        (ret.getHeader() != null ? ret.getHeader().getZmess() : ""));
            }

            // 无数据则退出循环
            if (CollectionUtils.isEmpty(ret.getItems())) {
                break;
            }

            // 转换为前端展示格式
            for (SapCustReconCtrRetItem item : ret.getItems()) {
                CustReconResultVO vo = new CustReconResultVO();
                BeanUtils.copyProperties(item, vo);
                resultList.add(vo);
            }

            pageNo++;
        }

        return resultList;
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
    private String buildCacheKey(String bdate, String edate, String bukrs, String kunnr) {
        return String.format("%s|%s|%s|%s", bdate, edate, bukrs, kunnr);
    }
}

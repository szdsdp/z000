package com.szd.z000.web.sap.mm.mtlStock.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientMdmReqCommon;
import com.szd.core.client.domain.common.ClientMdmReqCommonItem;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.security.ClientSecurity;
import com.szd.z000.base.orm.domain.mm.mtlStock.MtlStock;
import com.szd.z000.base.orm.mapper.mm.mtlStock.MtlStockMapper;
import com.szd.z000.common.sap.domain.mtlStock.SapMtlStockCtrRet;
import com.szd.z000.common.sap.domain.mtlStock.SapMtlStockCtrRetItem;
import com.szd.z000.common.sap.domain.service.SapMtlStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 物料库存查询-服务类
 */
@Service
public class MtlStockService {
    private static final Logger logger = LoggerFactory.getLogger(MtlStockService.class);

    @Resource
    private SapMtlStockService sapMtlStockService;
    @Resource
    private MtlStockMapper mtlStockMapper;
    @Resource
    private ClientCore clientCore;

    /**
     * 查询物料库存列表（从数据库）
     */
    public List<MtlStock> queryStockList(ClientRpParam param) {
        return mtlStockMapper.selectList(param);
    }

    /**
     * 同步库存：分页循环调用SAP查询并保存到数据库
     */
    @Transactional(rollbackFor = Exception.class)
    public ClientResult syncStock(String matnr, String werks, String lgort) {
        int totalCount = 0;
        int pageNo = 1;
        int pageSize = 100;

        // 循环分页调用SAP，直至无数据返回
        while (true) {
            // 构建SAP请求
            ClientMdmReqCommon req = new ClientMdmReqCommon();
            List<ClientMdmReqCommonItem> itemsRanges = new ArrayList<>();

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

            if (matnr != null && !matnr.isEmpty()) {
                ClientMdmReqCommonItem matnrItem = new ClientMdmReqCommonItem();
                matnrItem.setFilename("MATNR");
                matnrItem.setSign("I");
                matnrItem.setOption("EQ");
                matnrItem.setLow(matnr);
                itemsRanges.add(matnrItem);
            }
            if (werks != null && !werks.isEmpty()) {
                ClientMdmReqCommonItem werksItem = new ClientMdmReqCommonItem();
                werksItem.setFilename("WERKS");
                werksItem.setSign("I");
                werksItem.setOption("EQ");
                werksItem.setLow(werks);
                itemsRanges.add(werksItem);
            }
            if (lgort != null && !lgort.isEmpty()) {
                ClientMdmReqCommonItem lgortItem = new ClientMdmReqCommonItem();
                lgortItem.setFilename("LGORT");
                lgortItem.setSign("I");
                lgortItem.setOption("EQ");
                lgortItem.setLow(lgort);
                itemsRanges.add(lgortItem);
            }

            req.setItemsRanges(itemsRanges);

            SapMtlStockCtrRet ret = sapMtlStockService.callSap(req);
            if (ret == null) {
                throw new ClientCustomException("SAP物料库存查询接口调用失败");
            }
            if (ret.getHeader() == null || !"S".equals(ret.getHeader().getZstau())) {
                throw new ClientCustomException("SAP物料库存查询失败：" +
                        (ret.getHeader() != null ? ret.getHeader().getZmess() : ""));
            }

            // 无数据则退出循环
            if (CollectionUtils.isEmpty(ret.getItems())) {
                break;
            }

            // SAP已返回物料描述(MAKTX)和单位(MEINS)，直接使用
            String userId = ClientSecurity.getUserId();
            Date now = new Date();
            List<MtlStock> stockList = new ArrayList<>();
            for (SapMtlStockCtrRetItem item : ret.getItems()) {
                MtlStock stock = new MtlStock();
                stock.setMatnr(item.getMatnr());
                stock.setMaktx(item.getMaktx());
                stock.setWerks(item.getWerks());
                stock.setLgort(item.getLgort());
                stock.setLabst(item.getLabst() != null ? new BigDecimal(item.getLabst()) : BigDecimal.ZERO);
                stock.setSpeme(item.getSpeme() != null ? new BigDecimal(item.getSpeme()) : BigDecimal.ZERO);
                stock.setUnit(item.getMeins());
                stock.setCreateBy(userId);
                stock.setCreateTime(now);
                stockList.add(stock);
            }
            mtlStockMapper.batchUpsert(stockList);

            totalCount += ret.getItems().size();
            pageNo++;
        }

        ClientResult result = new ClientResult();
        result.setStatus("S");
        result.setMsg("同步成功，共" + totalCount + "条");
        return result;
    }

    /**
     * 构建库存查询请求（供盘点下发等场景复用）
     */
    public ClientMdmReqCommon buildStockReq(String werks, String lgort) {
        ClientMdmReqCommon req = new ClientMdmReqCommon();
        List<ClientMdmReqCommonItem> itemsRanges = new ArrayList<>();

        ClientMdmReqCommonItem pageNoItem = new ClientMdmReqCommonItem();
        pageNoItem.setFilename("PAGENO");
        pageNoItem.setSign("I");
        pageNoItem.setOption("EQ");
        pageNoItem.setLow("1");
        itemsRanges.add(pageNoItem);

        ClientMdmReqCommonItem pageSizeItem = new ClientMdmReqCommonItem();
        pageSizeItem.setFilename("PAGESIZE");
        pageSizeItem.setSign("I");
        pageSizeItem.setOption("EQ");
        pageSizeItem.setLow("999999999");
        itemsRanges.add(pageSizeItem);

        ClientMdmReqCommonItem werksItem = new ClientMdmReqCommonItem();
        werksItem.setFilename("WERKS");
        werksItem.setSign("I");
        werksItem.setOption("EQ");
        werksItem.setLow(werks);
        itemsRanges.add(werksItem);

        ClientMdmReqCommonItem lgortItem = new ClientMdmReqCommonItem();
        lgortItem.setFilename("LGORT");
        lgortItem.setSign("I");
        lgortItem.setOption("EQ");
        lgortItem.setLow(lgort);
        itemsRanges.add(lgortItem);

        req.setItemsRanges(itemsRanges);
        return req;
    }
}

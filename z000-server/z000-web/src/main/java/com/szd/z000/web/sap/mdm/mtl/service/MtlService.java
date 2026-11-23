package com.szd.z000.web.sap.mdm.mtl.service;

import com.szd.core.client.domain.common.ClientMdmReqCommon;
import com.szd.core.client.domain.common.ClientMdmReqCommonItem;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.util.ClientUtilDate;
import com.szd.z000.base.orm.domain.mdm.mtl.Mtl;
import com.szd.z000.base.orm.mapper.mdm.mtl.MtlMapper;
import com.szd.z000.common.sap.domain.mtl.SapMtlCtrItem;
import com.szd.z000.common.sap.domain.mtl.SapMtlCtrRet;
import com.szd.z000.common.sap.domain.service.SapMtlSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物料主数据-服务类
 */
@Service
public class MtlService {
    private static final Logger logger = LoggerFactory.getLogger(MtlService.class);

    @Resource
    private MtlMapper mtlMapper;
    @Resource
    private SapMtlSyncService sapMtlSyncService;

    public List<Mtl> selectList(ClientRpParam param) {
        return mtlMapper.selectList(param);
    }

    /**
     * 根据物料编码列表批量查询物料名称
     */
    public java.util.Map<String, String> getMtlNameMap(java.util.List<String> mtlCodes) {
        java.util.Map<String, String> map = new java.util.HashMap<>();
        if (mtlCodes != null && !mtlCodes.isEmpty()) {
            java.util.List<Mtl> list = mtlMapper.selectByCodes(mtlCodes);
            if (list != null) {
                for (Mtl mtl : list) {
                    map.put(mtl.getMtlCode(), mtl.getMtlName());
                }
            }
        }
        return map;
    }

    /**
     * 根据物料编码列表批量查询物料单位
     */
    public Map<String, String> getMtlUnitMap(java.util.List<String> mtlCodes) {
      Map<String, String> map = new HashMap<>();
        if (mtlCodes != null && !mtlCodes.isEmpty()) {
            java.util.List<Mtl> list = mtlMapper.selectByCodes(mtlCodes);
            if (list != null) {
                for (Mtl mtl : list) {
                    map.put(mtl.getMtlCode(), mtl.getUnit());
                }
            }
        }
        return map;
    }

    /**
     * 同步SAP物料主数据
     */
    public ClientResult sync(String bdate, String edate, String matnr, String mtart) {
        ClientResult result = new ClientResult();
        int totalCount = 0;   // 处理条数
        int pageNo = 1;       // 页码
        int pageSize = 100;   // 每页条数

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

            if ((bdate != null && !bdate.isEmpty()) || (edate != null && !edate.isEmpty())) {
                ClientMdmReqCommonItem dateItem = new ClientMdmReqCommonItem();
                dateItem.setFilename("ERDAT");
                dateItem.setSign("I");
                dateItem.setOption("BT");
                dateItem.setLow(bdate != null && !bdate.isEmpty() ? ClientUtilDate.strToSap(bdate) : "");
                dateItem.setHigh(edate != null && !edate.isEmpty() ? ClientUtilDate.strToSap(edate) : "");
                itemsRanges.add(dateItem);
            }

            if (matnr != null && !matnr.isEmpty()) {
                ClientMdmReqCommonItem matnrItem = new ClientMdmReqCommonItem();
                matnrItem.setFilename("MATNR");
                matnrItem.setSign("I");
                matnrItem.setOption("EQ");
                matnrItem.setLow(matnr);
                itemsRanges.add(matnrItem);
            }

            if (mtart != null && !mtart.isEmpty()) {
                ClientMdmReqCommonItem mtartItem = new ClientMdmReqCommonItem();
                mtartItem.setFilename("MTART");
                mtartItem.setSign("I");
                mtartItem.setOption("EQ");
                mtartItem.setLow(mtart);
                itemsRanges.add(mtartItem);
            }

            req.setItemsRanges(itemsRanges);

            // 调用SAP
            SapMtlCtrRet ret = sapMtlSyncService.callSap(req);
            if (ret == null) {
                throw new ClientCustomException("SAP物料主数据同步接口调用失败");
            }
            if (ret.getHeader() == null || !"S".equals(ret.getHeader().getZstau())) {
                throw new ClientCustomException("SAP物料主数据同步失败：" +
                        (ret.getHeader() != null ? ret.getHeader().getZmess() : ""));
            }
            // 无数据则退出循环
            if (CollectionUtils.isEmpty(ret.getItems())) {
                break;
            }

            // 映射为实体列表
            List<Mtl> mtlList = new ArrayList<>();
            for (SapMtlCtrItem item : ret.getItems()) {
                Mtl mtl = new Mtl();
                mtl.setMtlCode(item.getMatnr());
                mtl.setMtlName(item.getMaktx());
                mtl.setUnit(item.getMeins());
                mtl.setMtlType(item.getMtart());
                mtl.setProdGroup(item.getSpart());
                mtl.setMtlGroup(item.getMatkl());
                mtlList.add(mtl);
            }

            // 批量upsert（存在则更新，不存在则新增）
            mtlMapper.batchUpsert(mtlList);
            totalCount += mtlList.size();

            logger.info("物料主数据同步第{}页完成，处理{}条", pageNo, mtlList.size());
            pageNo++;
        }

        result.setStatus("S");
        result.setMsg("同步完成，共处理" + totalCount + "条");
        return result;
    }
}

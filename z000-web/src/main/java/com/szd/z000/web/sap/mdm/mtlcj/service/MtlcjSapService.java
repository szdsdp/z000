package com.szd.z000.web.sap.mdm.mtlcj.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.z000.base.orm.domain.mdm.mtl.Mtl;
import com.szd.z000.base.orm.domain.mdm.mtlcj.MtlcjBaseView;
import com.szd.z000.base.orm.domain.mdm.mtlcj.MtlcjHeader;
import com.szd.z000.base.orm.domain.mdm.mtlcj.MtlcjPurView;
import com.szd.z000.base.orm.mapper.mdm.mtlcj.MtlcjBaseViewMapper;
import com.szd.z000.base.orm.mapper.mdm.mtlcj.MtlcjHeaderMapper;
import com.szd.z000.base.orm.mapper.mdm.mtlcj.MtlcjPurViewMapper;
import com.szd.z000.base.orm.mapper.mdm.mtl.MtlMapper;
import com.szd.z000.common.sap.domain.mtlcj.*;
import com.szd.z000.common.sap.domain.service.SapMtlcjService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 物料主数据创建申请-SAP服务
 */
@Service
public class MtlcjSapService {
    private static final Logger logger = LoggerFactory.getLogger(MtlcjSapService.class);

    @Resource
    private MtlcjHeaderMapper mtlcjHeaderMapper;
    @Resource
    private MtlcjBaseViewMapper mtlcjBaseViewMapper;
    @Resource
    private MtlcjPurViewMapper mtlcjPurViewMapper;
    @Resource
    private MtlMapper mtlMapper;
    @Resource
    private SapMtlcjService sapMtlcjService;
    @Resource
    private ClientCore clientCore;

    /**
     * 推送SAP创建/修改物料主数据
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClientResult genSapMtlcj(String applyId) {
        ClientResult clientResult = new ClientResult();
        MtlcjHeader header = mtlcjHeaderMapper.selectById(applyId);
        if (header == null || !"D".equals(header.getBussStatus())) {
            return clientResult;
        }
        MtlcjBaseView baseView = mtlcjBaseViewMapper.selectByApplyId(applyId);
        MtlcjPurView purView = mtlcjPurViewMapper.selectByApplyId(applyId);

        // 封装SAP参数-抬头
        SapMtlcjCtr sapCtr = new SapMtlcjCtr();
        SapMtlcjCtrHeader ctrHeader = new SapMtlcjCtrHeader();
        ctrHeader.setIfDocno(header.getBussDocId());
        // 判断创建/修改：已有物料编码则为修改
        if (StringUtils.isNotBlank(header.getMtlCode())) {
            ctrHeader.setDtype("U");
            ctrHeader.setMaterial(header.getMtlCode());
        } else {
            ctrHeader.setDtype("C");
        }
        ctrHeader.setMatlDesc(header.getMtlName());
        ctrHeader.setIndSector(header.getIndSector());
        ctrHeader.setMatlType(header.getMtlType());
        // 基本视图始终传X
        ctrHeader.setBasicView("X");
        // 采购视图：工厂不为空时传X
        ctrHeader.setPurchaseView(purView != null && StringUtils.isNotBlank(purView.getFact()) ? "X" : "");
        sapCtr.setHeader(ctrHeader);

        // 封装SAP参数-基础视图
        if (baseView != null) {
            SapMtlcjCtrMara mara = new SapMtlcjCtrMara();
            mara.setBaseUom(baseView.getUnitBase());
            mara.setMatlGroup(baseView.getMtlGroup());
            mara.setSizeDim(baseView.getSizeDim());
            mara.setPoUnit(baseView.getUnitPo());
            mara.setNetWeight(baseView.getNetWt());
            mara.setUnitOfWt(baseView.getUnitWt());
            sapCtr.setMara(mara);
        }

        // 封装SAP参数-采购视图：仅工厂不为空时传递
        if (purView != null && StringUtils.isNotBlank(purView.getFact())) {
            SapMtlcjCtrMarc marc = new SapMtlcjCtrMarc();
            marc.setPlant(purView.getFact());
            marc.setAbcId(purView.getAbcId());
            marc.setPurGroup(purView.getPurGroup());
            marc.setIssueUnit(purView.getIssueUnit());
            marc.setSafetyStk(purView.getSafetyStk());
            marc.setMinlotsize(purView.getMinLotsize());
            marc.setMaxlotsize(purView.getMaxLotsize());
            marc.setFixedLot(purView.getFixedLot());
            marc.setRoundVal(purView.getRoundVal());
            marc.setMaxStock(purView.getMaxStock());
            sapCtr.setMarc(marc);
        }

        // 调用SAP接口创建/修改物料主数据
        clientResult = sapMtlcjService.createMtl(sapCtr, applyId);

        // SAP成功时同步写入物料主数据表
        if ("S".equals(clientResult.getStatus())) {
            Mtl mtl = new Mtl();
            mtl.setMtlCode(clientResult.getDocId());
            mtl.setMtlName(header.getMtlName());
            mtl.setMtlType(header.getMtlType());
            if (baseView != null) {
                mtl.setUnit(baseView.getUnitBase());
                mtl.setMtlGroup(baseView.getMtlGroup());
            }
            mtlMapper.batchUpsert(java.util.Collections.singletonList(mtl));
        }

        return clientResult;
    }
}

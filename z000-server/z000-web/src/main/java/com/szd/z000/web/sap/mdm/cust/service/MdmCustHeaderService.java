package com.szd.z000.web.sap.mdm.cust.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import com.szd.z000.base.orm.domain.mdm.cust.*;
import com.szd.z000.base.orm.mapper.mdm.cust.MdmCustHeaderMapper;
import com.szd.z000.web.sap.mdm.cust.domain.MdmCustDataVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.szd.z000.service.common.utils.Z000Util;


import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 运输单-头
 */
@Service
public class MdmCustHeaderService {
    private static final Logger logger = LoggerFactory.getLogger(MdmCustHeaderService.class);
    @Resource
    private MdmCustHeaderMapper mdmCustHeaderMapper;
    @Resource
    private ClientCore clientCore;
//    @Resource
//    private CheckBaseUtil checkBaseUtil;

    public List<MdmCustHeader> selectList(ClientRpParam param) {
        return mdmCustHeaderMapper.selectList(param);
    }

    public MdmCustHeader selectById(String bussId) {
        MdmCustHeader header = mdmCustHeaderMapper.selectById(bussId);
        if (header == null) {
            return new MdmCustHeader();
        }
        ClientWfBussBase wfBussBase = clientCore.wf.baseGetObj(bussId);
        if (wfBussBase != null) {
            BeanUtils.copyProperties(wfBussBase, header);
        }
        return header;
    }

    public MdmCustHeader selectObjById(String bussId) {
        MdmCustHeader header = mdmCustHeaderMapper.selectById(bussId);
        if (header == null) {
            return new MdmCustHeader();
        }
        return header;
    }

    /**
     * 保存抬头
     */
    public void insert(MdmCustHeader header) {
        mdmCustHeaderMapper.insert(header);
    }

    /***
     * 更新抬头
     */
    public void update(MdmCustHeader header) {
        mdmCustHeaderMapper.update(header);
    }

    public void updateStatusAndDocId(MdmCustHeader header) {
        mdmCustHeaderMapper.updateStatusAndDocId(header.getBussId(), header.getBussStatus(), header.getBussDocId());
    }

    /**
     * 抬头数据校验
     */
    public void checkData(MdmCustDataVO data) {
        MdmCustHeader header = data.getHeader();
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_BSTP", header.getBstp(), "业务类型", "1"));
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_CTRY", header.getCtry(), "国家", "1"));
        Z000Util.checkResult(clientCore.mdm.checkA("CORE_PROV", header.getProvinceCore(), "省份", "1"));
        Z000Util.checkResult(clientCore.mdm.checkB("CORE_CITY", header.getProvinceCore(), header.getCity(), "城市", "1"));
        if (header.getCustFlag() != null) {
            header.setCustFlagDB(String.join(",", header.getCustFlag().stream().map(String::valueOf).collect(Collectors.toList())));
        }
        if (!CollectionUtils.isEmpty(data.getBkacList())) {
            for (MdmCustBkac bkac : data.getBkacList()) {
                Z000Util.checkResult(clientCore.mdm.checkA("CORE_CNAP", bkac.getCnap(), "银行视图-联行号", "1"));
            }
        }
        if (!CollectionUtils.isEmpty(data.getCmpyKList())) {
            for (MdmCustCmpy cmpy : data.getCmpyKList()) {
                Z000Util.checkResult(clientCore.mdm.checkA("CORE_CMPY", cmpy.getCmpy(), "客户公司视图-公司代码", "1"));
            }
        }
        if (!CollectionUtils.isEmpty(data.getCmpyDList())) {
            for (MdmCustCmpy cmpy : data.getCmpyDList()) {
                Z000Util.checkResult(clientCore.mdm.checkA("CORE_CMPY", cmpy.getCmpy(), "供应商公司视图-公司代码", "1"));
            }
        }
//        if (!CollectionUtils.isEmpty(data.getSaleList())) {
//            for (SapCustSale sale : data.getSaleList()) {
//                for (SapCustSalePartner partner : sale.getPartnerList()) {
//                    checkBaseUtil.check("SASM_MDM_CUST", partner.getPartnerCode(), "销售视图-业务伙伴");
//                }
//            }
//        }
    }

    public void deleteById(String bussId) {
        mdmCustHeaderMapper.deleteById(bussId);
    }
}

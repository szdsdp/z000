package com.szd.z000.web.demo.expCost.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import com.szd.z000.base.orm.domain.demo.expCost.ExpCostHeader;
import com.szd.z000.base.orm.mapper.demo.expCost.ExpCostHeaderMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 业务抬头Service
 */
@Service
@Slf4j
public class ExpCostHeaderService {
    @Resource
    private ExpCostHeaderMapper expCostHeaderMapper;
    @Resource
    private ClientCore clientCore;

    /**
     * 查询清单
     */
    public List<ExpCostHeader> selectList(ClientRpParam param) {
        return expCostHeaderMapper.selectList(param);
    }

    /**
     * 查询抬头
     */
    public ExpCostHeader selectById(String bussId) {
        ExpCostHeader header = expCostHeaderMapper.selectById(bussId);
        if (header == null) {
            return new ExpCostHeader();
        }
        ClientWfBussBase wfBussBase = clientCore.wf.baseGetObj(bussId);
        if (wfBussBase != null) {
            BeanUtils.copyProperties(wfBussBase, header);
        }
        return header;
    }

    /**
     * 保存抬头
     */
    public void insert(ExpCostHeader header) {
        expCostHeaderMapper.insert(header);
    }

    /**
     * 更新抬头
     */
    public void update(ExpCostHeader costHeader) {
        expCostHeaderMapper.update(costHeader);
    }

    /**
     * 删除抬头
     */
    public void deleteById(String costId) {
        expCostHeaderMapper.deleteById(costId);
    }

    /**
     * 抬头数据校验
     */
    public void checkData(ExpCostHeader header) {
        header.setBstp(header.getBstp());
        clientCore.mdm.checkAE("CORE_USER", header.getUserId(), "用户", "1");
        clientCore.mdm.checkAE("CORE_CMPY", header.getCmpy(), "公司", "1");
        clientCore.mdm.checkBE("CORE_CSTC", header.getCmpy(), header.getCstc(), "部门", "1");
        clientCore.mdm.checkAE("CORE_BSTP", header.getBstp(), "业务类型", "1");
        clientCore.mdm.checkAE("CORE_CURR", header.getCurr(), "货币", "1");
        if (StringUtils.isBlank(header.getBussStatus()) || "0".equals(header.getBussStatus())) {
            ClientResult retResult = clientCore.wf.dyCheck(header.getBstp(), header.getUserId(), header.getWfDyId());
            header.setWfDyId(retResult.getDocId());
        }
    }

    public void updateStatusAndDocId(ExpCostHeader header) {
        expCostHeaderMapper.updateStatusAndDocId(header.getBussId(), header.getBussStatus(), header.getBussDocId());
    }

    /**
     * 获取字段描述-报表
     */
    public void getDescList(List<ExpCostHeader> list) {
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(header -> {
                header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "0"));
                header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "0"));
                header.setUserName(clientCore.mdm.getDescA("CORE_USER", header.getUserId(), "0"));
                header.setBstp(clientCore.mdm.getDescA("CORE_BSTP", header.getBstp(), "0"));
                header.setBussStatus(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "0"));
                header.setCreateBy(clientCore.mdm.getDescA("CORE_USER", header.getCreateBy(), "0"));
                header.setUpdateBy(clientCore.mdm.getDescA("CORE_USER", header.getUpdateBy(), "0"));
            });
        }
    }

    /**
     * 获取字段描述
     */
    public void getDesc(ExpCostHeader header) {
        header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "1"));
        header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "1"));
        header.setUserName(clientCore.mdm.getDescA("CORE_USER", header.getUserId(), "1"));
        header.setBstpName(clientCore.mdm.getDescA("CORE_BSTP", header.getBstp(), "1"));
        header.setCurrName(clientCore.mdm.getDescA("CORE_CURR", header.getCurr(), "1"));
        header.setCmpyCreateName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpyCreate(), "1"));
        header.setCstcCreateName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstcCreate(), "1"));
    }
}

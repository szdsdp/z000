package com.szd.z000.web.z1.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import com.szd.core.client.security.ClientSecurity;
import com.szd.z000.base.orm.domain.z1.Z1Header;
import com.szd.z000.base.orm.domain.z1.Z1RpParam;
import com.szd.z000.base.orm.mapper.z1.Z1HeaderMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 费用报销业务抬头Service
 */
@Service
@Slf4j
public class Z1HeaderService {
    @Resource
    private Z1HeaderMapper headerMapper;
    @Resource
    private ClientCore clientCore;

    public List<Z1Header> selectList(Z1RpParam param) {
        return headerMapper.selectList(param);
    }

    /**
     * 查询抬头
     */
    public Z1Header selectById(String costId) {
        Z1Header header = headerMapper.selectById(costId);
        if (header == null) {
            return new Z1Header();
        }
        ClientWfBussBase wfBussBase = clientCore.wf.baseGetObj(costId);
        if (wfBussBase != null) {
            BeanUtils.copyProperties(wfBussBase, header);
        }
        return header;
    }

    /**
     * 保存抬头
     */
    public int insert(Z1Header costHeader) {
        // 初始化状态
        costHeader.setBussStatus("A");
        costHeader.setCreateBy(ClientSecurity.getUserId());
        return headerMapper.insert(costHeader);
    }

    /**
     * 更新抬头
     */
    public int update(Z1Header costHeader) {
        return headerMapper.update(costHeader);
    }

    /**
     * 删除抬头
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(String costId) {
        return headerMapper.deleteById(costId);
    }

    /**
     * 抬头数据校验
     */
    public void checkData(Z1Header header) {
        header.setBstp(header.getBstp());
        clientCore.mdm.checkAE("CORE_USER", header.getUserId(), "用户", "1");
        clientCore.mdm.checkAE("CORE_CMPY", header.getCmpyCreate(), "制单人公司", "1");
        clientCore.mdm.checkBE("CORE_CSTC", header.getCmpyCreate(), header.getCstcCreate(), "制单人部门", "1");
        clientCore.mdm.checkAE("CORE_CMPY", header.getCmpy(), "公司", "1");
        clientCore.mdm.checkBE("CORE_CSTC", header.getCmpy(), header.getCstc(), "部门", "1");
        clientCore.mdm.checkAE("CORE_BSTP", header.getBstp(), "业务类型", "1");
        clientCore.mdm.checkAE("CORE_CURR", header.getCurr(), "货币", "1");
        if (StringUtils.isBlank(header.getBussStatus()) || "0".equals(header.getBussStatus())) {
            ClientResult retResult = clientCore.wf.dyCheck(header.getBstp(), header.getUserId(), header.getWfDyId());
            header.setWfDyId(retResult.getDocId());
        }
    }

    public void updateStatusAndDocId(Z1Header header) {
        headerMapper.updateStatusAndDocId(header.getBussId(), header.getBussStatus(), header.getBussDocId());
    }

    /**
     * 获取字段描述-报表
     */
    public void getDescList(List<Z1Header> list) {
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(header -> {
                header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "0"));
                header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "0"));
                header.setUserId(clientCore.mdm.getDescA("CORE_USER", header.getUserId(), "0"));
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
    public void getDesc(Z1Header header) {
        header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "1"));
        header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "1"));
        header.setUserName(clientCore.mdm.getDescA("CORE_USER", header.getUserId(), "1"));
        header.setBstpName(clientCore.mdm.getDescA("CORE_BSTP", header.getBstp(), "1"));
        header.setCurrName(clientCore.mdm.getDescA("CORE_CURR", header.getCurr(), "1"));
        header.setCmpyCreateName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpyCreate(), "1"));
        header.setCstcCreateName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstcCreate(), "1"));
    }
}

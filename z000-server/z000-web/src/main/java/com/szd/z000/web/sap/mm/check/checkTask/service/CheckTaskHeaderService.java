package com.szd.z000.web.sap.mm.check.checkTask.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.z000.base.orm.domain.mm.check.checkTask.CheckTaskHeader;
import com.szd.z000.base.orm.mapper.mm.check.checkTask.CheckTaskHeaderMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 盘点任务-抬头服务
 */
@Service
public class CheckTaskHeaderService {
    @Resource
    private CheckTaskHeaderMapper checkTaskHeaderMapper;
    @Resource
    private ClientCore clientCore;

    public List<CheckTaskHeader> selectList(ClientRpParam param) {
        return checkTaskHeaderMapper.selectList(param);
    }

    public CheckTaskHeader selectById(String taskId) {
        return checkTaskHeaderMapper.selectById(taskId);
    }

    public List<CheckTaskHeader> selectByPlanId(String planId) {
        return checkTaskHeaderMapper.selectByPlanId(planId);
    }

    public void insert(CheckTaskHeader header) {
        checkTaskHeaderMapper.insert(header);
    }

    public void update(CheckTaskHeader header) {
        checkTaskHeaderMapper.update(header);
    }

    public void updateStatusAndDocId(CheckTaskHeader header) {
        checkTaskHeaderMapper.updateStatusAndDocId(header.getTaskId(), header.getBussStatus(), header.getBussDocId());
    }

    public void checkData(CheckTaskHeader header) {
    }

    public void deleteById(String taskId) {
        checkTaskHeaderMapper.deleteById(taskId);
    }
}

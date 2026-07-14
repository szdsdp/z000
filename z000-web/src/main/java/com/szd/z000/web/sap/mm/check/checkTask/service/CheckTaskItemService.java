package com.szd.z000.web.sap.mm.check.checkTask.service;

import com.szd.z000.base.orm.domain.mm.check.checkTask.CheckTaskItem;
import com.szd.z000.base.orm.mapper.mm.check.checkTask.CheckTaskItemMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 盘点任务-行项目服务
 */
@Service
public class CheckTaskItemService {
    @Resource
    private CheckTaskItemMapper checkTaskItemMapper;

    public List<CheckTaskItem> selectList(String taskId) {
        return checkTaskItemMapper.selectList(taskId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(String taskId, List<CheckTaskItem> itemList) {
        checkTaskItemMapper.deleteByTaskId(taskId);
        if (itemList != null && !itemList.isEmpty()) {
            checkTaskItemMapper.batchInsert(itemList);
        }
    }

    public void deleteByTaskId(String taskId) {
        checkTaskItemMapper.deleteByTaskId(taskId);
    }
}

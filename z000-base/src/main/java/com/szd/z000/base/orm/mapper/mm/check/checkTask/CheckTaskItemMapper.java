package com.szd.z000.base.orm.mapper.mm.check.checkTask;

import com.szd.z000.base.orm.domain.mm.check.checkTask.CheckTaskItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 盘点任务-行项目Mapper
 */
public interface CheckTaskItemMapper {
    List<CheckTaskItem> selectList(String taskId);
    void batchInsert(List<CheckTaskItem> list);
    void deleteByTaskId(String taskId);
    void deleteByTaskIdAndItemNo(@Param("taskId") String taskId, @Param("itemNo") int itemNo);
    void updateItem(CheckTaskItem item);
}

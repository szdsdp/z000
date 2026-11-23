package com.szd.z000.base.orm.mapper.mm.check.checkTask;

import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.z000.base.orm.domain.mm.check.checkTask.CheckTaskHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 盘点任务-抬头Mapper
 */
public interface CheckTaskHeaderMapper {
    List<CheckTaskHeader> selectList(ClientRpParam param);
    CheckTaskHeader selectById(String taskId);
    void insert(CheckTaskHeader header);
    void update(CheckTaskHeader header);
    void updateStatusAndDocId(@Param("taskId") String taskId, @Param("bussStatus") String bussStatus, @Param("bussDocId") String bussDocId);
    void updateTaskStatus(@Param("taskId") String taskId, @Param("taskStatus") String taskStatus);
    void deleteById(String taskId);
    List<CheckTaskHeader> selectByPlanId(String planId);
}

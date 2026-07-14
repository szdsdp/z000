package com.szd.z000.service.prog.job;

import com.szd.core.client.annotation.ClientProgJob;
import com.szd.core.client.annotation.ClientProgJobInf;
import com.szd.core.client.domain.job.ClientJobTaskProParam;
import com.szd.core.client.domain.job.ClientJobTaskRet;
import com.szd.z000.base.common.service.RedisCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 定时任务测试
 */
@Service
@ClientProgJob(proCode = "Z000_JOB_001", proName = "Z000定时任务测试001")
public class ProgJobTest01 implements ClientProgJobInf {
    private static final Logger logger = LoggerFactory.getLogger(ProgJobTest01.class);
    @Resource
    private RedisCache redisCache;

    @Override
    public ClientJobTaskRet process(ClientJobTaskProParam clientJobTaskProParam) {
        ClientJobTaskRet clientJobTaskRet = new ClientJobTaskRet();
        try {
            logger.info("执行Z000_JOB_001业务逻辑..........开始");
            // 此处模拟作业业务逻辑处理

            clientJobTaskRet.setStatus("S");
            clientJobTaskRet.setMsg("执行成功");
            logger.info("执行Z000_JOB_001业务逻辑..........完成");
        } catch (Exception e) {
            logger.error("执行异常", e);
            clientJobTaskRet.setStatus("E");
            clientJobTaskRet.setMsg("执行异常");
        }
        return clientJobTaskRet;
    }
}

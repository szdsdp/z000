package com.szd.z000.service.common.cache;

import com.szd.z000.base.common.service.RedisCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 缓存工具服务
 * @author dlzal
 */
@Service
public class CacheService {
    @Resource
    private RedisCache redisCache;

    /**
     * 增加缓存
     * @param key 缓存key
     * @param field Map key
     * @param val Map 值
     */
    public void addCache(String key, String field, String val) {
        // 写缓存
        Map<String, String> map = new HashMap<>(16);
        map.put(field, val);
        redisCache.setCacheMap(key, map);
        map = null;
    }

    /**
     * 更新缓存，先删后写
     */
    public void updateCache(String key, String field, String val) {
        delCache(key, field);
        addCache(key, field, val);
    }

    /**
     * 删除缓存
     */
    public void delCache(String key, String field) {
        redisCache.deleteMapKey(key, field);
    }

    /**
     * 刷新缓存，先删后写
     */
    public void refreshCache(String cacheKey, Map map) {
        redisCache.deleteObject(cacheKey);
        redisCache.setCacheMap(cacheKey, map);
    }
}

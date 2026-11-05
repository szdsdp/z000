package com.szd.z000.web.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.function.Supplier;

/**
 * Session缓存工具类
 * <p>
 * 用于缓存SAP等外部接口的全量查询结果。同一查询条件下翻页时直接复用缓存，
 * 避免每次翻页都重复调用外部接口。
 * </p>
 *
 * <pre>
 * 使用示例：
 * List&lt;MyVO&gt; allItems = SessionCacheUtil.getOrFetch(
 *     session, "myBusiness", cacheKey,
 *     () -&gt; callSapLoop(params)
 * );
 * // 然后对 allItems 做内存分页即可
 * </pre>
 */
public class SessionCacheUtil {

    private static final Logger logger = LoggerFactory.getLogger(SessionCacheUtil.class);

    /** session属性key前缀，避免与业务属性冲突 */
    private static final String KEY_PREFIX = "SC_";

    /**
     * 缓存条目
     */
    public static class CacheEntry<T> {
        /** 查询条件指纹 */
        private final String cacheKey;
        /** 缓存的数据列表 */
        private final List<T> data;

        public CacheEntry(String cacheKey, List<T> data) {
            this.cacheKey = cacheKey;
            this.data = data;
        }

        public String getCacheKey() {
            return cacheKey;
        }

        public List<T> getData() {
            return data;
        }
    }

    /**
     * 从session获取缓存数据，未命中时通过fetcher获取并写入缓存
     *
     * @param session  当前HttpSession（自动按用户隔离）
     * @param namespace 命名空间（区分不同业务模块，如"payClear"、"purOrder"）
     * @param cacheKey 查询条件指纹（条件不变则命中缓存，条件变化则自动清理并重新获取）
     * @param fetcher  数据获取逻辑（仅缓存未命中时执行）
     * @param <T>      数据类型
     * @return 数据列表
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getOrFetch(HttpSession session, String namespace,
                                          String cacheKey, Supplier<List<T>> fetcher) {
        String sessionAttrKey = KEY_PREFIX + namespace;
        CacheEntry<T> entry = (CacheEntry<T>) session.getAttribute(sessionAttrKey);

        // 命中缓存：条件指纹一致，直接复用
        if (entry != null && cacheKey.equals(entry.getCacheKey())) {
            logger.info("[SessionCache] {} 命中缓存，跳过数据获取，缓存 {} 条", namespace, entry.getData().size());
            return entry.getData();
        }

        // 未命中：清理旧缓存
        if (entry != null) {
            logger.info("[SessionCache] {} 查询条件变化，清理旧缓存", namespace);
            session.removeAttribute(sessionAttrKey);
        }

        // 执行实际数据获取
        List<T> data = fetcher.get();
        logger.info("[SessionCache] {} 获取到 {} 条数据，写入session缓存", namespace, data.size());

        // 写入新缓存
        session.setAttribute(sessionAttrKey, new CacheEntry<>(cacheKey, data));

        return data;
    }

}

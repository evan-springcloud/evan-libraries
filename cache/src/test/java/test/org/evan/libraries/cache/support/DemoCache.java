package test.org.evan.libraries.cache.support;

import net.sf.ehcache.CacheManager;
import org.evan.libraries.cache.AbstractCache;
import org.evan.libraries.redis.RedisTemplateCreator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @author evan.shen
 */
public class DemoCache extends AbstractCache<Demo> {
    private final static String CACHE_NAME = DemoCache.class.getSimpleName();

    private CacheManager cacheManager;
    private RedisTemplateCreator redisTemplateCreator;

    @PostConstruct
    public void init() {
        super.setRedisExpireSeconds(5);
        super.init(CACHE_NAME, redisTemplateCreator, 1);
    }

    @Autowired
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Autowired
    public void setRedisTemplateCreator(RedisTemplateCreator redisTemplateCreator) {
        this.redisTemplateCreator = redisTemplateCreator;
    }
}

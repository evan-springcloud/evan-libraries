package test.org.evan.libraries.redis.support.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.evan.libraries.redis.RedisInitUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Evan.Shen
 * @since 2019-08-13
 */
@Configuration
@EnableCaching
public class RedisConfig {
    private static StringRedisSerializer keySerializer = new StringRedisSerializer();
    private static GenericFastJsonRedisSerializer valueSerializer = new GenericFastJsonRedisSerializer();

    @Autowired
    private LettuceConnectionFactory lettuceConnectionFactory;

    @Bean
    public CacheManager cacheManager() {
        // 配置序列化（解决乱码的问题）
//        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(timeToLive)
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(ValueSerializer))
//                .disableCachingNullValues();


        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(lettuceConnectionFactory)
                //.cacheDefaults(config)
                .transactionAware();

        return builder.build();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        return RedisInitUtil.createRedisTemplate(lettuceConnectionFactory,keySerializer,valueSerializer,keySerializer,valueSerializer);
    }
}
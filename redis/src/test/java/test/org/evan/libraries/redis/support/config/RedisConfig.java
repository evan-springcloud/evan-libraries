package test.org.evan.libraries.redis.support.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
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
    private static GenericFastJsonRedisSerializer ValueSerializer = new GenericFastJsonRedisSerializer();

    static {
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        //ValueSerializer.setObjectMapper(om);
    }

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


        // 配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(lettuceConnectionFactory);

        redisTemplate.setKeySerializer(keySerializer);// key序列化
        redisTemplate.setValueSerializer(ValueSerializer);// value序列化
        redisTemplate.setHashKeySerializer(keySerializer);// Hash key序列化
        redisTemplate.setHashValueSerializer(ValueSerializer);// Hash value序列化

        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }


//    private RedisSerializer<String> keySerializer() {
//        return new StringRedisSerializer();
//    }
//
//    private RedisSerializer<Object> valueSerializer() {
//        // 设置序列化
//        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
//                Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//        return jackson2JsonRedisSerializer;
//
//        //或者使用GenericJackson2JsonRedisSerializer
//        //return new GenericJackson2JsonRedisSerializer();
//    }
}

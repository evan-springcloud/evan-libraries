package org.evan.libraries.redis;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author Evan.Shen
 * @since 2019-08-18
 */
public class RedisInitUtil {

//    static {
////        ObjectMapper om = new ObjectMapper();
////        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
////        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        //ValueSerializer.setObjectMapper(om);
//    }

    public static RedisTemplate<String, Object> createRedisTemplate(
            RedisConnectionFactory redisConnectionFactory,
            RedisSerializer keySerializer, RedisSerializer valueSerializer, RedisSerializer hashKeySerializer, RedisSerializer hashValueSerializer) {
        // 配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);

        redisTemplate.setKeySerializer(keySerializer);// key序列化
        redisTemplate.setValueSerializer(valueSerializer);// value序列化
        redisTemplate.setHashKeySerializer(hashKeySerializer);// Hash key序列化
        redisTemplate.setHashValueSerializer(hashValueSerializer);// Hash value序列化

        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

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

package org.evan.libraries.redis.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * @author Evan.Shen
 * @since 2019-06-19
 */
public class GenericFastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");



    public GenericFastJson2JsonRedisSerializer() {
        super();
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        FastJsonWraper<T> wraperSet = new FastJsonWraper<>(t);
        return JSON.toJSONString(wraperSet, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        String deserializeStr = new String(bytes, DEFAULT_CHARSET);
        FastJsonWraper<T> wraperGet = JSON.parseObject(deserializeStr, FastJsonWraper.class);
        return wraperGet.getValue();
    }
}

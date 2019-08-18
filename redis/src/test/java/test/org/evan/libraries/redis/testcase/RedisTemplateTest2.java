package test.org.evan.libraries.redis.testcase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import test.org.evan.libraries.redis.support.RedisTestCaseSupport;
import test.org.evan.libraries.redis.support.model.Demo;

/**
 * @author Evan.Shen
 * @since 2019-08-13
 */
public class RedisTemplateTest2 extends RedisTestCaseSupport {

    private final static int KEY_COUNT = 8;//16个redis key
    private final static int HASH_KEY_COUNT = 8;//每个redis key 8个 hash key

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test() {
        long begin = System.currentTimeMillis();
        LOGGER.info("begin: {} ", begin);
        long end;
        long begin1 = 0;

        for (int i = 0; i < KEY_COUNT; i++) {
            BoundHashOperations<String, String, Demo> hashOperations = redisTemplate.boundHashOps("key_demo" + i);

            for (int j = 0; j < HASH_KEY_COUNT; j++) {
//                RedisTemplate redisTemplate = redisTemplates.get(j);
                redisTemplate.execute(new RedisCallback() {
                    @Override
                    public Object doInRedis(RedisConnection connection) throws DataAccessException {
                        return null;
                    }
                });

                Demo demo = new Demo(Long.valueOf(j));
                demo.setFieldText(j + "");

                hashOperations.put("hkey" + j, demo);

                LOGGER.info("put demo: id:{}, text:{}", demo.getId(), demo.getFieldText());
            }
        }
        end = System.currentTimeMillis();
        LOGGER.info("timespan:{} ", end - begin1);

        for (int i = 0; i < KEY_COUNT; i++) {
            BoundHashOperations<String, String, Demo> hashOperations = redisTemplate.boundHashOps("key_demo" + i);

            for (int j = 0; j < HASH_KEY_COUNT; j++) {
                Demo o = hashOperations.get("hkey" + j);
                if (o != null) {
                    LOGGER.info("get demo: id:{}, text:{}", o.getId(), o.getFieldText());
                }
            }
        }
    }
}

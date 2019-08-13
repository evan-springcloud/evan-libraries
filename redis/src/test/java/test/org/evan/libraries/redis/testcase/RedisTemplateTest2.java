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
    private final static int DATABASE_COUNT = 16;//模拟16个库测试

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test() {
        long begin = System.currentTimeMillis();
        System.out.println(begin);
        long end;
        long begin1 = 0;

        for (int i = 0; i < 10; i++) {
            BoundHashOperations<String, String, Demo> hashOperations = redisTemplate.boundHashOps("demo" + i);

            for (int j = 0; j < DATABASE_COUNT; j++) {
//                RedisTemplate redisTemplate = redisTemplates.get(j);
                redisTemplate.execute(new RedisCallback() {
                    @Override
                    public Object doInRedis(RedisConnection connection) throws DataAccessException {
                        return null;
                    }
                });

                Demo demo = new Demo(Long.valueOf(j));
                demo.setFieldText(j + "");

                hashOperations.put("key" + j, demo);
            }
            if (i % 1000 == 0) {
                end = System.currentTimeMillis();
                System.out.println(end - begin1);
                begin1 = end;
            }
        }
        end = System.currentTimeMillis();
        System.out.println(end - begin);

        for (int i = 0; i < 100; i++) {
            BoundHashOperations<String, String, Demo> hashOperations = redisTemplate.boundHashOps("demo" + i);

            for (int j = 0; j < DATABASE_COUNT; j++) {
                Demo o = hashOperations.get("key" + j);
                if(o != null) {
                    LOGGER.info(o.getClass().getName() + o);
                }
            }
        }
    }
}

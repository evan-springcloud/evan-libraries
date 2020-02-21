package test.org.evan.libraries.utils.testcase;

import org.evan.libraries.utils.AESException;
import org.evan.libraries.utils.AESUtil;
import org.junit.Test;

/**
 * AESUtil Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Feb 21, 2020</pre>
 */
public class AESUtilTest {

    /**
     * Method: encrypt(String data, String secret)
     */
    @Test
    public void test() throws AESException {

        String a = AESUtil.encrypt("112233", "123");
        System.out.println(a);

        String b = AESUtil.decrypt(a, "123");
        System.out.println(b);
    }

} 

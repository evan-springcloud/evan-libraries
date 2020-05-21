package test.org.evan.libraries.utils;

import org.junit.Test;

import java.io.File;

/**
 * @author Evan.Shen
 * @since 2020-03-19
 */
public class FileUtilTest {

    @Test
    public void test(){
        File file = new File("/Users/evan/数脉/m公司事务/申报/ISO-技术相关/附件1 软件项目开发（模板）");
        File[] aa = file.listFiles();
        for (int i = 0; i < aa.length; i++) {
            if (aa[i].isFile()) {
                System.out.println(aa[i]);
            }
        }
    }
}

package test.org.evan.libraries.utils.testcase;

import org.evan.libraries.utils.BeanUtil;
import org.junit.Test;
import test.org.evan.libraries.utils.testdata.Demo;
import test.org.evan.libraries.utils.testdata.DemoData;

/**
 * BeanUtil Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Dec 12, 2019</pre>
 */
public class BeanUtilTest {

    /**
     * Method: quickMap(Object source, Class<T> targetClass)
     */
    @Test
    public void testQuickMap() {
        Demo demo = DemoData.random();
        Demo demo2 = BeanUtil.quickMap(demo, Demo.class);
    }

    /**
     * Method: quickMapList(Collection<?> sourceList, Class<T> targetClass)
     */
    @Test
    public void testQuickMapList() {
//TODO: Test goes here... 
    }

    /**
     * Method: quickCopy(Object source, Object target)
     */
    @Test
    public void testQuickCopy() {
//TODO: Test goes here... 
    }

    /**
     * Method: adviceMap(Object source, Class<T> destinationClass)
     */
    @Test
    public void testAdviceMap() {
        Demo demo = DemoData.random();
        Demo demo2 = BeanUtil.adviceMap(demo, Demo.class);
    }

    /**
     * Method: adviceMapList(Collection<?> sourceList, Class<T> destinationClass)
     */
    @Test
    public void testAdviceMapList() {
//TODO: Test goes here... 
    }

    /**
     * Method: adviceCopy(Object source, Object destinationObject)
     */
    @Test
    public void testAdviceCopy() {
//TODO: Test goes here... 
    }

    /**
     * Method: beanToMap(Object bean)
     */
    @Test
    public void testBeanToMap() {
//TODO: Test goes here... 
    }

    /**
     * Method: beanToMap2(Object bean)
     */
    @Test
    public void testBeanToMap2() {
//TODO: Test goes here... 
    }

    /**
     * Method: getClassName(Object po)
     */
    @Test
    public void testGetClassName() {
//TODO: Test goes here... 
    }

    /**
     * Method: getObjectName(Class<?> c)
     */
    @Test
    public void testGetObjectName() {
//TODO: Test goes here... 
    }


    /**
     * Method: getBeanCopier(Class<?> sourceClass, Class<?> targetClass)
     */
    @Test
    public void testGetBeanCopier() {
//TODO: Test goes here... 
/* 
try { 
   Method method = BeanUtil.getClass().getMethod("getBeanCopier", Class<?>.class, Class<?>.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

} 

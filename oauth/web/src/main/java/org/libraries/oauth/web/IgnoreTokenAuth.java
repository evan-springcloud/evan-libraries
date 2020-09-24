package org.libraries.oauth.web;

/**
 * @author Evan.Shen
 * @since 2020-09-24
 */

import java.lang.annotation.*;

/**
 * 忽略token验证<br>
 * 在Controller方法上加上该注解，表示该方法不需要token验证<br>
 * 在Controller类上加上该注解，表示该类所有方法不需要登录验证
 * <p>
 * create at 2014年7月30日 下午8:44:52
 * @author shen.wei
 * @version %I%, %G%
 *
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface IgnoreTokenAuth {
}

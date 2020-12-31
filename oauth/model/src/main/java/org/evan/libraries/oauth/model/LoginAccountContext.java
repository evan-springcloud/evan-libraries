package org.evan.libraries.oauth.model;


/**
 * 登录账号读取器
 */
public class LoginAccountContext {
    protected static ThreadLocal<LoginAccount> threadLocal = new ThreadLocal<>();

    public static LoginAccount get() {
        return threadLocal.get();
    }
}

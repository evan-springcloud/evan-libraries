package org.evan.libraries.web.session;


import org.evan.libraries.model.AbstractLoginAccount;

/**
 * 获取登录用户
 */
public class LoginAccountContext {
    private static ThreadLocal<AbstractLoginAccount> threadLocal = new ThreadLocal<>();

    public static AbstractLoginAccount get() {
        return threadLocal.get();
    }

    public static void put(AbstractLoginAccount userAgent) {
        threadLocal.set(userAgent);
    }

    public static void remove() {
        threadLocal.remove();
    }
}

package org.evan.libraries.rest.session;


import org.evan.libraries.model.CurrentLoginAccount;

/**
 * 获取登录用户
 */
public class LoginAccountContext {
    private static ThreadLocal<CurrentLoginAccount> threadLocal = new ThreadLocal<>();

    public static CurrentLoginAccount get() {
        return threadLocal.get();
    }

    public static void put(CurrentLoginAccount userAgent) {
        threadLocal.set(userAgent);
    }

    public static void remove() {
        threadLocal.remove();
    }
}

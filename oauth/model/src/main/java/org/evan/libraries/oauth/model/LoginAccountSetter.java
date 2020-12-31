package org.evan.libraries.oauth.model;

/**
 * @author Evan.Shen
 * @since 2019-11-26
 */
public class LoginAccountSetter extends LoginAccountContext {
    public static void put(LoginAccount loginAccount) {
        threadLocal.set(loginAccount);
    }

    public static void remove() {
        threadLocal.remove();
    }
}

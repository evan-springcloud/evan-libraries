package org.evan.libraries.oauth.web;

import org.evan.libraries.oauth.model.LoginAccount;

import javax.servlet.http.HttpServletRequest;


/**
 * create at 2017年4月15日 上午12:33:42
 *
 * @author shen.wei
 * @version %I%, %G%
 * @since 1.5
 */
public  interface LoginSession<T extends LoginAccount> {
    T get(HttpServletRequest request);

    String getTokenSecret(String token);
}

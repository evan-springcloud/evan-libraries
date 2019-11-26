package org.libraries.oauth.web;

import org.libraries.oauth.model.LoginAccount;

import javax.servlet.http.HttpServletRequest;


/**
 * create at 2017年4月15日 上午12:33:42
 *
 * @author shen.wei
 * @version %I%, %G%
 */
public abstract class AbstractLoginAccountSession<T extends LoginAccount> {
    public abstract T get(HttpServletRequest request);
}

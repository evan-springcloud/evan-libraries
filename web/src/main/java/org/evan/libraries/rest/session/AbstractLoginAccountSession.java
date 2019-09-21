package org.evan.libraries.rest.session;

import org.evan.libraries.model.CurrentLoginAccount;

import javax.servlet.http.HttpServletRequest;


/**
 * create at 2014年4月15日 上午12:33:42
 *
 * @author shen.wei
 * @version %I%, %G%
 */
public abstract class AbstractLoginAccountSession<T extends CurrentLoginAccount> implements LoginAccountSession {

    @Override
    public abstract T get(HttpServletRequest request);
}

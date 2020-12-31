package org.evan.libraries.oauth.model;

import java.util.Date;

/**
 * 当前登录账号
 *
 * @author Evan.Shen
 * @since 2019-11-26
 */
public interface LoginAccount {
    Long getId();

    String getType();


    String getAccount();


    /**
     *
     */
    String getToken();


    /**
     *
     */
    String getRemoteAddr();


    /**
     *
     */
    String getStatus();


    /**
     *
     */
    String getTokenSecret();


    /**
     *
     */
    Date getLastLoginTime();


}

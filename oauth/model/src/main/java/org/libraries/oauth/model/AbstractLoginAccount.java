package org.libraries.oauth.model;


import java.util.Date;

/**
 * 当前登录账号抽象类，不同的账号类型继承该类
 *
 * @author Evan.Shen
 * @since 2019-11-26
 */
public abstract class AbstractLoginAccount implements LoginAccount {

    private Long id;
    private String account;
    private String remoteAddr;
    private String status;

    private Date lastLoginTime;

    private String token;
    private String tokenSecret;

    public abstract String getType();

    /**
     *
     */
    public Long getId() {
        return id;
    }

    /***/
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     */
    public String getAccount() {
        return account;
    }

    /***/
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     *
     */
    public String getToken() {
        return token;
    }

    /***/
    public void setToken(String token) {
        this.token = token;
    }

    /**
     *
     */
    public String getRemoteAddr() {
        return remoteAddr;
    }

    /***/
    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    /**
     *
     */
    public String getStatus() {
        return status;
    }

    /***/
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     */
    public String getTokenSecret() {
        return tokenSecret;
    }

    /***/
    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    /**
     *
     */
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    /***/
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @Override
    public String toString() {
        return "AbstractLoginAccount{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", remoteAddr='" + remoteAddr + '\'' +
                ", status='" + status + '\'' +
                ", lastLoginTime=" + lastLoginTime +
                ", token='" + token + '\'' +
                '}';
    }
}

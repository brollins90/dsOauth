package com.oauth.authorization.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class AuthorizationCode implements Serializable {

    @Id
    @Column(nullable = false)
    private String authorizationCode;

    @Column(nullable = false)
    private String clientId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private long createTime;

    public AuthorizationCode() {
    }

    public AuthorizationCode(String authorizationCode, String clientId, String username) {
        this(authorizationCode, clientId, username, new Date().getTime());
    }

    public AuthorizationCode(String authorizationCode, String clientId, String username, long createTime) {
        this.authorizationCode = authorizationCode;
        this.clientId = clientId;
        this.username = username;
        this.createTime = createTime;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}

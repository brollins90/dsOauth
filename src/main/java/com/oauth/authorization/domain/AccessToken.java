package com.oauth.authorization.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class AccessToken implements Serializable {

    @Id
    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private String clientId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String tokenType;

    @Column(nullable = false)
    private Long expiration;

    @Column(nullable = false)
    private String scope;

    public AccessToken() {
    }

    public AccessToken(String accessToken, String clientId, String username, String tokenType, Long expiration, String scope) {
        this.accessToken = accessToken;
        this.clientId = clientId;
        this.username = username;
        this.tokenType = tokenType;
        this.expiration = expiration;
        this.scope = scope;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
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

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}

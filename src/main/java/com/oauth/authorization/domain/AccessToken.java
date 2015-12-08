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

    public String getClientId() {
        return clientId;
    }

    public String getUsername() {
        return username;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Long getExpiration() {
        return expiration;
    }

    public String getScope() {
        return scope;
    }
}

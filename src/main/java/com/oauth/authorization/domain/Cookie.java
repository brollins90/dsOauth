package com.oauth.authorization.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Cookie implements Serializable {

    @Id
    @Column(nullable = false)
    private String cookie;

    @Column(nullable = false)
    private String username;

    public Cookie() {
    }

    public Cookie(String cookie, String username) {
        this.cookie = cookie;
        this.username = username;
    }

    public String getCookie() {
        return cookie;
    }

    public String getUsername() {
        return username;
    }
}

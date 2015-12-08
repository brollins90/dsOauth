package com.oauth.authorization.domain;

import com.oauth.authorization.domain.Cookie;
import org.springframework.data.repository.Repository;

public interface CookieRepository extends Repository<Cookie, String> {

    Cookie findByCookie(String cookie);
}

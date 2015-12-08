package com.oauth.authorization.service;

import com.oauth.authorization.domain.Cookie;

public interface CookieService {

    Cookie findByCookie(String cookie);

    Cookie createCookie(String username);
}

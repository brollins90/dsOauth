package com.oauth.authorization.service;

import com.oauth.authorization.domain.Cookie;
import com.oauth.authorization.domain.CookieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Component("cookieService")
public class CookieServiceImpl implements CookieService {

    private final CookieRepository cookieRepository;

    @Autowired
    public CookieServiceImpl(CookieRepository cookieRepository) {
        this.cookieRepository = cookieRepository;
    }

    @Override
    public Cookie findByCookie(String cookie) {
        Assert.hasLength(cookie, "cookie cannot be empty");
        return this.cookieRepository.findByCookie(cookie);
    }

    @Override
    public Cookie createCookie(String username) {
        Cookie cookie = new Cookie(
                this.generateCookieString(),
                username);

        return null;
    }

    private String generateCookieString() {
        return "YumACookie";
    }
}

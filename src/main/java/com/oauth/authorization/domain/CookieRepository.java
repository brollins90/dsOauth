package com.oauth.authorization.domain;

import org.springframework.data.repository.CrudRepository;


public interface CookieRepository extends CrudRepository<Cookie, String> {

    Cookie findByCookie(String cookie);
}

package com.oauth.authorization.domain;

import org.springframework.data.repository.CrudRepository;

public interface AccessTokenRepository extends CrudRepository<AccessToken, String> {

    AccessToken findByAccessToken(String accessToken);

    Iterable<AccessToken> findByUsername(String username);
}

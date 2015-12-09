package com.oauth.authorization.service;

import com.oauth.authorization.domain.AccessToken;

public interface AccessTokenService {

    AccessToken findByAccessToken(String accessToken);

    Iterable<AccessToken> findByUsername(String username);

    AccessToken createAccessToken(String clientId, String username, String scope);
}

package com.oauth.authorization.service;

import com.oauth.authorization.domain.AuthorizationCode;

public interface AuthorizationCodeService {

    AuthorizationCode findByAuthorizationCode(String authorizationCode);

    AuthorizationCode createAuthorizationCode(String clientId, String username);
}

package com.oauth.authorization.domain;

import com.oauth.authorization.domain.AuthorizationCode;
import org.springframework.data.repository.Repository;

public interface AuthorizationCodeRepository extends Repository<AuthorizationCode, String> {

    AuthorizationCode findByAuthorizationCode(String authorizationCode);
}

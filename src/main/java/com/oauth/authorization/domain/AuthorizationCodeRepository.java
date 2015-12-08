package com.oauth.authorization.domain;

import org.springframework.data.repository.CrudRepository;

public interface AuthorizationCodeRepository extends CrudRepository<AuthorizationCode, String> {

    AuthorizationCode findByAuthorizationCode(String authorizationCode);
}

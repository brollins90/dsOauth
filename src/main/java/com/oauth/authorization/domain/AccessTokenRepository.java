package com.oauth.authorization.domain;

import com.oauth.authorization.domain.AccessToken;
import org.springframework.data.repository.Repository;

public interface AccessTokenRepository extends Repository<AccessToken, String> {

    AccessToken findByAccessToken(String accessToken);
}

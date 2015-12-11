package com.oauth.authorization.service;

import com.oauth.authorization.domain.AccessToken;
import com.oauth.authorization.domain.AccessTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Component("accessTokenService")
public class AccessTokenServiceImpl implements AccessTokenService {

    private final AccessTokenRepository accessTokenRepository;

    @Autowired
    public AccessTokenServiceImpl(AccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
    }

    @Override
    public AccessToken findByAccessToken(String accessToken) {
        Assert.hasLength(accessToken, "accessToken cannot be empty");
        return this.accessTokenRepository.findByAccessToken(accessToken);
    }

    @Override
    public Iterable<AccessToken> findByUsername(String username) {
        Assert.hasLength(username, "username cannot be empty");
        return this.accessTokenRepository.findByUsername(username);
    }

    @Override
    public AccessToken createAccessToken(String clientId, String username, String scope) {
        if (scope == null) scope = "email";
        AccessToken accessToken = new AccessToken(
                this.generateAccessTokenString(username),
                clientId,
                username,
                "exapleTokenType",
                3600l,
                scope);
        return this.accessTokenRepository.save(accessToken);
    }

    private String generateAccessTokenString(String username) {
        return "AndThisIsAToken"+username;
    }

	@Override
	public void delete(String accessToken) {
		accessTokenRepository.delete(accessToken);
	}
}

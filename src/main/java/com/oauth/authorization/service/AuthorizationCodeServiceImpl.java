package com.oauth.authorization.service;

import com.oauth.authorization.domain.AuthorizationCode;
import com.oauth.authorization.domain.AuthorizationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Component("authorizationCodeService")
public class AuthorizationCodeServiceImpl implements AuthorizationCodeService {

    private final AuthorizationCodeRepository authorizationCodeRepository;

    @Autowired
    public AuthorizationCodeServiceImpl(AuthorizationCodeRepository authorizationCodeRepository) {
        this.authorizationCodeRepository = authorizationCodeRepository;
    }

    @Override
    public AuthorizationCode findByAuthorizationCode(String authorizationCode) {
        Assert.hasLength(authorizationCode, "authorizationCode cannot be empty");
        return this.authorizationCodeRepository.findByAuthorizationCode(authorizationCode);
    }

    @Override
    public AuthorizationCode createAuthorizationCode(String clientId, String username) {
        AuthorizationCode authorizationCode = new AuthorizationCode(
                this.generateAuthorizationCodeString(username),
                clientId,
                username);
        return this.authorizationCodeRepository.save(authorizationCode);
    }

    private String generateAuthorizationCodeString(String username) {
//        return Base64.getEncoder().encodeToString((clientID + UUID.randomUUID()).getBytes());
        return "BLAKEISCOOL"+username;
    }
}

package com.oauth.authorization.model;

import java.util.List;

public interface AuthorizationDB {
    public boolean isValidClientID(String clientID);
    public String getClientSecret(String clientID);

    void SaveAuthCode(String authCode, String clientId, long time);

    boolean isValidRedirectUrl(String clientId, String redirectUri);

    boolean isValidCode(String clientId, String code);

    String generateAccessToken(String clientId, String code);
}

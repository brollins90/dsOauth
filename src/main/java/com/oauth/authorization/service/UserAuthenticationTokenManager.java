package com.oauth.authorization.service;

import com.oauth.authorization.domain.User;

public interface UserAuthenticationTokenManager {
    public String generateAuthToken(User user);
    public boolean validateAuthToken(String authToken);
    public User getUserFromToken(String authToken);
}

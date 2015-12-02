package com.oauth.fakebookApplication.model;

import com.oauth.fakebookApplication.model.implementation.FakebookUser;

public interface UserAuthenticationTokenManager {
    public String generateAuthToken(FakebookUser user);
    public boolean validateAuthToken(String authToken);
    public FakebookUser getUserFromToken(String authToken);
}

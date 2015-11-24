package com.oauth.authorization.model;

import java.util.List;

public interface AuthorizationDB {
    public boolean isValidClientID(String clientID);
    public String getClientSecret(String clientID);
}

package com.oauth.authorization.model;

import com.oauth.authorization.model.implementation.*;

public interface AuthorizationDB {
    // client stuff
	public Client getClient(String clientId);
	public void addClient(Client client);
    public boolean isValidClientID(String clientID);
    public String getClientSecret(String clientID);

    void SaveAuthCode(String authCode, String clientId, long time);

    boolean isValidRedirectUrl(String clientId, String redirectUri);


    boolean isValidCode(String clientId, String code);

    String generateAccessToken(String clientId, String code);


    // user stuff
    public User getUser(String username);
    public void addUser(User user);
    public void updateUser(String username, User user);
    public User getUser(String username, String password);
}

package com.oauth.authorization.model.implementation;

import com.oauth.authorization.model.AuthorizationDB;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FakeAuthorizationDB implements AuthorizationDB{


    private HashMap<String, Client> Clients;
    private HashMap<String, User> Users;

    public FakeAuthorizationDB(){
        Clients = new HashMap<>();

        Client client1 = new Client();
        client1.setClientSecret("xoxoxo");
        client1.setClientId("clientid1");
        client1.setClientName("clientid1");
        client1.setClientRedirectUrl("http://localhost:5000/callback");
        client1.setClientPostLogoutRedirectUrl("http://localhost:5000/oauthlogout");
        client1.setFlow(Flow.AuthorizationCode);
        client1.getAllowedScopes().add("name");
        client1.getAllowedScopes().add("email");
        client1.getAllowedScopes().add("phone");

        Clients.put(client1.getClientId(), client1);


        Users = new HashMap<>();

        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("password");
        user1.setName("User Name");
        user1.setEmail("user1@example.com");
        user1.setPhone("111-111-1111");

        Users.put(user1.getUsername(), user1);

    }
    @Override
    public boolean isValidClientID(String clientID) {
        return Clients.containsKey(clientID);
    }

    @Override
    public String getClientSecret(String clientId) {
        return Clients.get(clientId).getClientSecret();
    }

    @Override
    public void SaveAuthCode(String authCode, String clientId, long time) {

    }

    @Override
    public boolean isValidRedirectUrl(String clientId, String redirectUri) {
        return Clients.get(clientId).getClientRedirectUrl().equalsIgnoreCase(redirectUri);
    }
}

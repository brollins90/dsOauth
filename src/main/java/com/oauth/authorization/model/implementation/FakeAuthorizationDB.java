package com.oauth.authorization.model.implementation;

import com.oauth.authorization.model.AuthorizationDB;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;

public class FakeAuthorizationDB implements AuthorizationDB{
    private HashMap<String, String> clientSecretMap;
    private ArrayList<String> clientIDList;

    public FakeAuthorizationDB(){
        clientSecretMap = new HashMap<>();
        clientIDList = new ArrayList<>();

        clientSecretMap.put("clientid1","xoxoxo");
        clientSecretMap.put("clientid2","zzzzzz");

        clientIDList.add("clientid1");
        clientIDList.add("clientid2");
    }
    @Override
    public boolean isValidClientID(String clientID) {
        return clientIDList.contains(clientID);
    }

    @Override
    public String getClientSecret(String clientID) {
        return clientSecretMap.get(clientID);
    }
}

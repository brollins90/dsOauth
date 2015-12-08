//package com.oauth.authorization.model.implementation;
//
//import com.oauth.authorization.domain.Client;
//import com.oauth.authorization.domain.ClientType;
//import com.oauth.authorization.domain.Flow;
//import com.oauth.authorization.domain.User;
//import com.oauth.authorization.model.AuthorizationDB;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class FakeAuthorizationDB implements AuthorizationDB {
//
//
//    private HashMap<String, Client> Clients;
//    private HashMap<String, User> Users;
//
//    public FakeAuthorizationDB() {
//        Clients = new HashMap<>();
//
//        Client client1 = new Client(
//                "clientid1",
//                "clientName",
//                "xoxoxo",
//                "http://localhost:5000/login/oauthlogout",
//                "http://localhost:5000/login/oauthcallback",
//                new ArrayList<String>() {{
//                    add("username");
//                    add("email");
//                    add("homephone");
//                }},
//                Flow.AuthorizationCode,
//                ClientType.Confidential
//        );
//        Clients.put(client1.getClientId(), client1);
//
//
//        Users = new HashMap<>();
//
//        User user1 = new User("user1", "password", "Users actual Name", "user1@example.com", "111-111-1111");
//        Users.put(user1.getUsername(), user1);
//
//    }
//
//    @Override
//    public boolean isValidClientID(String clientID) {
//        return Clients.containsKey(clientID);
//    }
//
//    @Override
//    public String getClientSecret(String clientId) {
//        return Clients.get(clientId).getClientSecret();
//    }
//
//    @Override
//    public void SaveAuthCode(String authCode, String clientId, long time) {
//
//    }
//
//    @Override
//    public boolean isValidRedirectUrl(String clientId, String redirectUri) {
//        return Clients.get(clientId).getClientRedirectUrl().equalsIgnoreCase(redirectUri);
//    }
//
//    @Override
//    public boolean isValidCode(String clientId, String code) {
//        return true;
//    }
//
//    @Override
//    public String generateAccessToken(String clientId, String code) {
//        return "token111111";
//    }
//
//    @Override
//    public User getUser(String userId) {
//        return Users.get(userId);
//    }
//
//    @Override
//    public void addUser(User user) {
//        Users.put(user.getUsername(), user);
//    }
//
//    @Override
//    public void updateUser(String userId, User user) {
//        addUser(user);
//    }
//
//    @Override
//    public User getUser(String username, String password) {
//        User u = Users.get(username);
//        return (u.getPassword().equals(password)) ? u : null;
//    }
//}

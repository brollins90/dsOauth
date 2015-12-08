package com.oauth.authorization.service;

import com.oauth.authorization.domain.User;

public interface UserService {

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);
    
    void addUser(User user);
    
    void updateUser(User user, String userName);
}

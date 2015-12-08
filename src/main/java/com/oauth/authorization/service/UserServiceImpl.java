package com.oauth.authorization.service;

import com.oauth.authorization.domain.User;
import com.oauth.authorization.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Component("userService")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByUsername(String username) {
        Assert.hasLength(username, "Username must not be empty");
        return this.userRepository.findByUsername(username);
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        Assert.hasLength(username, "Username must not be empty");
        Assert.hasLength(password, "Password must not be empty");
        return this.userRepository.findByUsernameAndPassword(username, password);
    }
}

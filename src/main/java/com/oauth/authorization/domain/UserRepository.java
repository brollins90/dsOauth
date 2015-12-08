package com.oauth.authorization.domain;

import com.oauth.authorization.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface UserRepository extends Repository<User, String> {

    Page<User> findAll(Pageable pageable);

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);
}

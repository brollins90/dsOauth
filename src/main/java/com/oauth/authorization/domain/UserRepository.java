package com.oauth.authorization.domain;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);

}

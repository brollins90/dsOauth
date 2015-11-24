package com.oauth.config;

import com.oauth.authorization.model.AuthorizationDB;
import com.oauth.authorization.model.implementation.FakeAuthorizationDB;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan()
public class appConfig {

    @Bean
    public AuthorizationDB authorizationDB() {
      return new FakeAuthorizationDB();
  }
}

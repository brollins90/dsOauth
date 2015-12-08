package com.oauth.config;

import com.oauth.fakebookApplication.model.UserAuthenticationTokenManager;
import com.oauth.fakebookApplication.model.implementation.JWTUserAuthenticationTokenManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//import com.oauth.authorization.model.AuthorizationDB;
//import com.oauth.authorization.model.implementation.FakeAuthorizationDB;
import com.oauth.fakebookApplication.model.FakebookDB;
import com.oauth.fakebookApplication.model.implementation.Database;


@Configuration
@ComponentScan()
public class appConfig extends WebMvcConfigurerAdapter {

//    @Bean
//    public AuthorizationDB authorizationDB() {
//      return new FakeAuthorizationDB();
//  }
    
    @Bean
    public FakebookDB fakebookDB() {
    	return new Database();
    }

    @Bean
    public UserAuthenticationTokenManager getUserAuthenticationTokenManager() {
        JWTUserAuthenticationTokenManager atm = new JWTUserAuthenticationTokenManager();
        return atm;
    }

}

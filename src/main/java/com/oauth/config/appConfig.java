package com.oauth.config;

import com.oauth.authorization.service.UserAuthenticationTokenManager;
import com.oauth.authorization.service.JWTUserAuthenticationTokenManager;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//import com.oauth.authorization.model.AuthorizationDB;
//import com.oauth.authorization.model.implementation.FakeAuthorizationDB;
import com.oauth.fakebookApplication.model.FakebookDB;
import com.oauth.fakebookApplication.model.implementation.Database;
import org.h2.server.web.WebServlet;



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

    @Bean
    ServletRegistrationBean h2servletRegistration(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean( new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }
}

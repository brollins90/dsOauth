package com.oauth.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.oauth.authorization.model.AuthorizationDB;
import com.oauth.authorization.model.implementation.FakeAuthorizationDB;
import com.oauth.fakebookApplication.model.FakebookDB;
import com.oauth.fakebookApplication.model.implementation.Database;


@Configuration
@ComponentScan()
public class appConfig {

    @Bean
    public AuthorizationDB authorizationDB() {
      return new FakeAuthorizationDB();
  }
    
    @Bean
    public FakebookDB fakebookDB() {
    	return new Database();
    }
    
    @Bean
    public ViewResolver viewResolver() {
    	InternalResourceViewResolver resolver = new InternalResourceViewResolver();
    	Properties p = new Properties();
    	p.setProperty("prefix", "/WEB-INF/pages/");
    	p.setProperty("suffix", ".jsp");
    	resolver.setAttributes(p);
    	return resolver;
    }
}

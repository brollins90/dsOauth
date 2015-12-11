package com.oauth;

import com.oauth.authorization.domain.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@SpringBootApplication
public class DsOauthApplication {
    public static void main(String[] args) {
        SpringApplication.run(DsOauthApplication.class, args);

    }

    @Bean
    public CommandLineRunner demo(ClientRepository clientRepository, UserRepository userRepository) {
        return (args) -> {
            clientRepository.save(new Client(
                    "clientid1",
                    "Client with id 1",
                    "xoxoxo",
                    "http://localhost:5000/login/oauthlogout",
                    "http://localhost:5000/login/oauthcallback",
                    new ArrayList<String>() {{
                        add("username");
                        add("email");
                        add("homephone");
                    }},
                    Flow.AuthorizationCode,
                    ClientType.Confidential
            ));
            userRepository.save(new User(
                    "user1",
                    "password",
                    "john doe",
                    "user1@gmail.com",
                    "111-111-1111"
            ));
            userRepository.save(new User(
                    "user2",
                    "password",
                    "jayne smith",
                    "user2@gmail.com",
                    "222-222-2222"
            ));
        };
    }
}

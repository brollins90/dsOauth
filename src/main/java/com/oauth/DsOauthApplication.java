package com.oauth;

import com.oauth.authorization.domain.ClientRepository;
import com.oauth.authorization.domain.Client;
import com.oauth.authorization.domain.ClientType;
import com.oauth.authorization.domain.Flow;
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
    public CommandLineRunner demo(ClientRepository clientRepository) {
        return (args) -> {
            clientRepository.save(new Client(
                    "clientid1",
                    "clientName",
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
        };
    }
}

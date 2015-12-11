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
            clientRepository.save(new Client(
                    "dvJQD6aVAuG-!NspHuci4ktpw9TnldOz?skbUlt9",
                    "pointless",
                    "wHO?3fX=@geWDKJt1lug@C2IF9P=Z=OKemR3Z@qpBpDJDnYqfnr@1ZkhwBqx3weM5CMyiK=U4.jcUzhc_12hqFCRvWw3WOTQrUjL-nORgJ-iQ.?FSgGu:I5IdK_cFND2",
                    "logmeout",
                    "http://dsclient.transvec.com/complete/dsoauth2/",
                    new ArrayList<String>() {{
                        add("email");
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

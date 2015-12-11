package com.oauth.authorization.web;

import com.oauth.authorization.service.AccessTokenService;
import com.oauth.authorization.service.ClientService;
import com.oauth.authorization.service.UserAuthenticationTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ApiController {

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private UserAuthenticationTokenManager userAuthenticationTokenManager;


    @RequestMapping(path = "/api/user", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity authorize(
            @RequestParam(value = "access_token", required = false) String access_token,
            HttpServletRequest request) {

        if (access_token == null) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity("You are Cool", HttpStatus.OK);
    }
}

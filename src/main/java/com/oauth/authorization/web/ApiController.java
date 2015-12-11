package com.oauth.authorization.web;

import com.oauth.authorization.domain.AccessToken;
import com.oauth.authorization.domain.User;
import com.oauth.authorization.service.AccessTokenService;
import com.oauth.authorization.service.ClientService;
import com.oauth.authorization.service.UserAuthenticationTokenManager;
import com.oauth.authorization.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ApiController {

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private UserService userService;


    @RequestMapping(path = "/api/user", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity authorize(
            @RequestHeader(value = "Authorization", required = false) String bearer_token,
            HttpServletRequest request) {

        String tok = bearer_token.substring(7);
        System.out.println("/api/user?bearer_token=" + tok);
        if (bearer_token == null) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        AccessToken realToken = accessTokenService.findByAccessToken(tok);
        if (realToken != null) {
            User u = userService.findByUsername(realToken.getUsername());
            ApiResponse resp = new ApiResponse();
            if (realToken.getScope().indexOf("username") != -1) resp.username = u.getUsername();
            if (realToken.getScope().indexOf("email") != -1) resp.email = u.getEmail();
            if (realToken.getScope().indexOf("phone") != -1) resp.phone = u.getPhone();
            return new ResponseEntity(resp, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }
}

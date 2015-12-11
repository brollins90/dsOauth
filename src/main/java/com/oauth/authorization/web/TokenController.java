package com.oauth.authorization.web;

import com.oauth.authorization.service.AccessTokenService;
import com.oauth.authorization.service.AuthorizationCodeService;
import com.oauth.authorization.service.ClientService;
import com.oauth.authorization.service.CookieService;
import com.oauth.authorization.domain.AccessToken;
import com.oauth.authorization.domain.AuthorizationCode;
import com.oauth.authorization.domain.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private AuthorizationCodeService authorizationCodeService;

    @Autowired
    private ClientService clientService;
//
//    @Autowired
//    private CookieService cookieService;

    @RequestMapping(path = "/oauth/token", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity token(@RequestParam(value = "client_id", required = true) String client_id,
                                @RequestParam(value = "client_secret", required = false) String client_secret,
                                @RequestParam(value = "code", required = false) String code,
                                @RequestParam(value = "grant_type", required = true) String grant_type,
                                @RequestParam(value = "password", required = false) String password,
                                @RequestParam(value = "redirect_uri", required = true) String redirect_uri,
                                @RequestParam(value = "scope", required = false) String scope,
                                @RequestParam(value = "state", required = false) String state,
                                @RequestParam(value = "username", required = false) String username) {
        TokenParameters parameters = new TokenParameters();
        parameters.client_id = client_id;
        parameters.client_secret = client_secret;
        parameters.code = code;
        parameters.grant_type = grant_type;
        parameters.password = password;
        parameters.redirect_uri = redirect_uri;
        parameters.scope = scope;
        parameters.state = state;
        parameters.username = username;

        System.out.println("token endpoint");

        if (parameters.grant_type.equalsIgnoreCase("authorization_code")) { // AuthorizationCode Flow Step 2
            return doGenerateTokenFromAuthCode(parameters);
        } else if (parameters.grant_type.equalsIgnoreCase("client_credentials")) { // ClientCredentials Flow
            return doGenerateTokenFromClientCredentials(parameters);
        } else if (parameters.grant_type.equalsIgnoreCase("password")) { // ResourceOwner Flow
            return doGenerateTokenFromPassword(parameters);
        } else {
            return new ResponseEntity("Invalid grant_type", HttpStatus.BAD_REQUEST);
        }
    }

    protected ResponseEntity<AccessTokenResponse> doGenerateTokenFromClientCredentials(TokenParameters parameters) {
        return null;
    }

    protected ResponseEntity<AccessTokenResponse> doGenerateTokenFromAuthCode(TokenParameters parameters) {

        HttpHeaders responseHeaders = new HttpHeaders();
        AuthorizationCode authorizationCode = authorizationCodeService.findByAuthorizationCode(parameters.code);

        if (authorizationCode != null) {
        	if(authorizationCode.getClientId().equals(parameters.client_id) &&
        	   authorizationCode.getUsername().equals(parameters.username)) {
        		Client client = clientService.findClient(parameters.client_id);
        		if(client.getClientSecret().equals(parameters.client_secret) &&
        		   client.getAllowedScopes().contains(parameters.scope)) {
        			
        			 AccessToken accessToken = accessTokenService.createAccessToken(
        	                    parameters.client_id,
        	                    authorizationCode.getUsername(),
        	                    parameters.scope);

        	            responseHeaders.add("Content-Type", "application/json;charset=UTF-8");
        	            responseHeaders.add("Cache-Control", "no-store");
        	            responseHeaders.add("Pragma", "no-cache");

        	            AccessTokenResponse tokenResponse = new AccessTokenResponse();
        	            tokenResponse.access_token = accessToken.getAccessToken();
        	            tokenResponse.token_type = accessToken.getTokenType();
        	            tokenResponse.expires_in = accessToken.getExpiration().toString();
        	            tokenResponse.scope = accessToken.getScope();
        	            // TODO: refresh tokens
        	            tokenResponse.refresh_token = "";
        	            return new ResponseEntity<>(tokenResponse, responseHeaders, HttpStatus.OK);
        		}
        		else {
                    return doTokenError(parameters, "access_denied: invalid secret or scope");
        		}
        	}
        	else {
                return doTokenError(parameters, "access_denied: incorrect client_id or username");
        	}
           
        } else {
            return doTokenError(parameters, "access_denied: bad authorization code");
        }
    }
    // token response example
    /*HTTP/1.1 200 OK
     Content-Type: application/json;charset=UTF-8
     Cache-Control: no-store
     Pragma: no-cache

     {
       "access_token":"2YotnFZFEjr1zCsicMWpAA",
       "token_type":"example",
       "expires_in":3600,
       "refresh_token":"tGzv3JOkF0XG5Qx2TlKWIA",
       "example_parameter":"example_value"
     }*/


    protected ResponseEntity<AccessTokenResponse> doGenerateTokenFromPassword(TokenParameters parameters) {

        return null;
//        HttpHeaders responseHeaders = new HttpHeaders();
//
//        //check the user's credentials somewhere...
//        if (db.isValidClientID(parameters.getClientId())) {
//            String authToken = generateAuthorizationCode(parameters.getClientId());
//            responseHeaders.add("location", parameters.getRedirectUri() + "?access_token=" + authToken);
//            return new ResponseEntity<>(responseHeaders, HttpStatus.FOUND);
//        } else {
//            return doTokenError(parameters, "access_denied");
//        }
    }

    protected ResponseEntity doTokenError(TokenParameters parameters, String error) {

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("location", parameters.redirect_uri + "?error=" + error);
        return new ResponseEntity(responseHeaders, HttpStatus.OK);
    }
}

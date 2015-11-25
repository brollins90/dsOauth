package com.oauth.authorization.controller;

import com.oauth.authorization.model.AuthorizationDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/oauth")
public class OauthController {

    @Autowired
    AuthorizationDB db;

    /**
    client_id -	string - Required. The client ID that you received from DigitalOcean when you registered.
    redirect_uri -	string	- Required. Must match the callback URL that you supplied during application registration. The callback URL where users will be sent after authorization.
    response_type -	string - Required. Must be set to "code" to request an authorization code.
    scope -	string	- Optional. If not provided, scope defaults to "read". The valid scopes are listed below.
    state - string	- Recommended. An unguessable random string, used to protect against request forgery attacks.
     */
    @RequestMapping(path = "/authorize", method = RequestMethod.GET)
    public ResponseEntity<Void> authorize(
            @RequestParam("response_type") String response_type,
            @RequestParam("client_id") String client_id,
            @RequestParam("redirect_uri") String redirect_uri,
            @RequestParam("scope") String scope,
            @RequestParam("state") String state) {

        AuthorizeParameters parameters = new AuthorizeParameters();
        parameters.setClientId(client_id);
        parameters.setRedirectUri(redirect_uri);
        parameters.setResponseType(response_type);
        parameters.setScope(scope);
        parameters.setState(state);

        return doAuthorize(parameters);
    }

    protected ResponseEntity<Void> doAuthorize(AuthorizeParameters parameters){

        HttpHeaders responseHeaders = new HttpHeaders();

        //check the user's credentials somewhere...
        if(db.isValidClientID(parameters.getClientId())) {
            String authToken = generateAuthorizationCode(parameters.getClientId());
            responseHeaders.add("location", parameters.getRedirectUri() + "?code=" + authToken);
            return new ResponseEntity<Void>(responseHeaders, HttpStatus.TEMPORARY_REDIRECT);
        } else {
            responseHeaders.add("location", parameters.getRedirectUri() + "?error=access_denied");
            return new ResponseEntity<Void>(responseHeaders, HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Use the code from above in your access token request, which is a POST request to the token endpoint with the appropriate parameters.
     grant_type -	string -	Required. Must be set to "authorization_code" for an access token request.
     code -	string -	Required. The code that you received as a response to Step 1.
     client_id -	string -	Required. The client ID that you received from DigitalOcean when you registered.
     client_secret -	string -	Required. The client secret that you received from DigitalOcean when you registered.
     redirect_uri -	string -	Required. Must match the callback URL that you supplied during application registration.
     */
    @RequestMapping(path = "/token", method = RequestMethod.POST)
    public String generateToken(@RequestParam("grant_type") String grant_type,
                                @RequestParam("code") String code,
                                @RequestParam("client_id") String client_id,
                                @RequestParam("client_secret") String client_secret,
                                @RequestParam("redirect_uri") String redirect_uri) {
        return "generateToken";
    }

    private String generateAuthorizationCode(String clientID) {
       return Base64.getEncoder().encodeToString((clientID + UUID.randomUUID()).getBytes());
    }
}

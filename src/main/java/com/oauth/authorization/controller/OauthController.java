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
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/oauth")
public class OauthController {

    @Autowired
    AuthorizationDB db;

    /**
     * client_id -	string - Required. The client ID that you received from DigitalOcean when you registered.
     * redirect_uri -	string	- Required. Must match the callback URL that you supplied during application registration. The callback URL where users will be sent after authorization.
     * response_type -	string - Required. Must be set to "code" to request an authorization code.
     * scope -	string	- Optional. If not provided, scope defaults to "read". The valid scopes are listed below.
     * state - string	- Recommended. An unguessable random string, used to protect against request forgery attacks.
     */
    @RequestMapping(path = "/authorize", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Void> authorize(
            @RequestParam(value = "response_type", required = true) String response_type,
            @RequestParam(value = "client_id", required = true) String client_id,
            @RequestParam(value = "redirect_uri", required = false) String redirect_uri,
            @RequestParam(value = "scope", required = false) String scope,
            @RequestParam(value = "state", required = false) String state) {

        AuthorizeParameters parameters = new AuthorizeParameters();
        parameters.setClientId(client_id);
        parameters.setRedirectUri(redirect_uri);
        parameters.setResponseType(response_type);
        parameters.setScope(scope);
        parameters.setState(state);

        if (parameters.getResponseType().equalsIgnoreCase("code")) {
            return doAuthorizeCode(parameters);
        } else if (parameters.getResponseType().equalsIgnoreCase("token")) {
            return doAuthorizeToken(parameters);
        } else {
            return null;///???
        }
    }

    protected ResponseEntity<Void> doAuthorizeCode(AuthorizeParameters parameters) {

        HttpHeaders responseHeaders = new HttpHeaders();
        String state = (parameters.getState() != null) ? "&state=" + parameters.getState() : "";

        // check the client id
        if (db.isValidClientID(parameters.getClientId())) {

            // check the resource matches URL

            // check if scope is allowed (both on resource and allow the the user has given the permission)
            String authCode = generateAuthorizationCode(parameters.getClientId());
            db.SaveAuthCode(authCode, parameters.getClientId(), new Date().getTime());

            responseHeaders.add("location", parameters.getRedirectUri() + "?code=" + authCode + state);
            return new ResponseEntity<Void>(responseHeaders, HttpStatus.TEMPORARY_REDIRECT);
        } else {
            responseHeaders.add("location", parameters.getRedirectUri() + "?error=access_denied" + state);
            return new ResponseEntity<Void>(responseHeaders, HttpStatus.FORBIDDEN);
        }
    }

    protected ResponseEntity<Void> doAuthorizeToken(AuthorizeParameters parameters) {

        HttpHeaders responseHeaders = new HttpHeaders();
        String state = (parameters.getState() != null) ? "&state=" + parameters.getState() : "";

        // check that the grant_type is 'authorization_code'
        // check the client id
        if (db.isValidClientID(parameters.getClientId())) {

            // check that the code matches and hasnt expired
            String authToken = generateAuthorizationCode(parameters.getClientId());
            // check the url matches

            //todo make this a application/x-www-form-urlencoded encoded object with the code and the state as the pieces of information
            responseHeaders.add("location", parameters.getRedirectUri() + "?code=" + authToken + state);
            return new ResponseEntity<>(responseHeaders, HttpStatus.TEMPORARY_REDIRECT);
        } else {
            responseHeaders.add("location", parameters.getRedirectUri() + "?error=" + HttpStatus.FORBIDDEN + state);

            //todo make this a application/x-www-form-urlencoded encoded object with 'error' 'error_description' state
            return new ResponseEntity<>(responseHeaders, HttpStatus.FORBIDDEN);
        }
    }

    protected String generateAuthorizationCode(String clientID) {
        return Base64.getEncoder().encodeToString((clientID + UUID.randomUUID()).getBytes());
    }

    /**
     * Use the code from above in your access token request, which is a POST request to the token endpoint with the appropriate parameters.
     * grant_type -	string -	Required. Must be set to "authorization_code" for an access token request.
     * code -	string -	Required. The code that you received as a response to Step 1.
     * client_id -	string -	Required. The client ID that you received from DigitalOcean when you registered.
     * client_secret -	string -	Required. The client secret that you received from DigitalOcean when you registered.
     * redirect_uri -	string -	Required. Must match the callback URL that you supplied during application registration.
     */
    @RequestMapping(path = "/token", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Void> token(@RequestParam(value = "client_id", required = true) String client_id,
                                      @RequestParam(value = "client_secret", required = false) String client_secret,
                                      @RequestParam(value = "code", required = true) String code,
                                      @RequestParam(value = "grant_type", required = true) String grant_type,
                                      @RequestParam(value = "redirect_uri", required = true) String redirect_uri) {
        TokenParameters parameters = new TokenParameters();
        parameters.setClientId(parameters.getClientId());
        parameters.setClientSecret(parameters.getClientSecret());
        parameters.setCode(parameters.getCode());
        parameters.setGrantType(parameters.getGrantType());
        parameters.setRedirectUri(parameters.getRedirectUri());
        parameters.setState(parameters.getState());

        return doGenerateToken(parameters);
    }

    protected ResponseEntity<Void> doGenerateToken(TokenParameters parameters) {

        HttpHeaders responseHeaders = new HttpHeaders();

        //check the user's credentials somewhere...
        if (db.isValidClientID(parameters.getClientId())) {
            String authToken = generateAuthorizationCode(parameters.getClientId());
            responseHeaders.add("location", parameters.getRedirectUri() + "?access_token=" + authToken);
            return new ResponseEntity<>(responseHeaders, HttpStatus.TEMPORARY_REDIRECT);
        } else {
            return doTokenError(parameters, "access_denied");
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

    protected ResponseEntity<Void> doTokenError(TokenParameters parameters, String error) {

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("location", parameters.getRedirectUri() + "?error=" + error);
        return new ResponseEntity<>(responseHeaders, HttpStatus.FORBIDDEN);
    }
}

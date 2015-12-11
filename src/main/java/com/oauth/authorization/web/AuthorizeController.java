package com.oauth.authorization.web;

import java.io.IOException;

import com.oauth.authorization.domain.AccessToken;
import com.oauth.authorization.domain.AuthorizationCode;
import com.oauth.authorization.domain.Client;
import com.oauth.authorization.domain.User;
import com.oauth.authorization.service.AccessTokenService;
import com.oauth.authorization.service.AuthorizationCodeService;
import com.oauth.authorization.service.ClientService;
import com.oauth.authorization.service.CookieService;
import com.oauth.authorization.service.UserAuthenticationTokenManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthorizeController {

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private AuthorizationCodeService authorizationCodeService;

    @Autowired
    private ClientService clientService;

//    @Autowired
//    private CookieService cookieService;
//    
    @Autowired
    private UserAuthenticationTokenManager atm;


    @RequestMapping(path = "/oauth/authorize", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity authorize(
            @RequestParam(value = "client_id", required = false) String client_id,
            @RequestParam(value = "redirect_uri", required = false) String redirect_uri,
            @RequestParam(value = "response_type", required = false) String response_type,
            @RequestParam(value = "scope", required = false) String scope,
            @RequestParam(value = "state", required = false) String state,
            @CookieValue(value = "Auth-Token", defaultValue = "") String authToken,
            HttpServletRequest request) {

        AuthorizeParameters parameters = new AuthorizeParameters();
        parameters.client_id = client_id;
        parameters.redirect_uri = redirect_uri;
        parameters.response_type = response_type;
        parameters.scope = scope;
        parameters.state = (state != null) ? "&state=" + state : "";

        StringBuilder sb = new StringBuilder();
        sb.append("/oauth/authorize?");
        if (parameters.client_id != null) sb.append("client_id=" + parameters.client_id + "&");
        if (parameters.redirect_uri != null) sb.append("redirect_uri=" + parameters.redirect_uri + "&");
        if (parameters.response_type != null) sb.append("response_type=" + parameters.response_type + "&");
        if (parameters.scope != null) sb.append("scope=" + parameters.scope + "&");
        if (parameters.state != null) sb.append("state=" + parameters.state + "&");
        System.out.println(sb.toString().substring(0, sb.toString().length()-2));

        // because 400 and 500 error codes are not valid in the oauth spec, we need to not require
        // the required attributes so we can redirect the response instead of the normal spring way
//        if (parameters.redirect_uri == null) { // TODO: how do we redirect them if the redirect_uri is not here?
//            HttpHeaders responseHeaders = new HttpHeaders();
//            responseHeaders.add("location",
//                    parameters.redirect_uri
//                            + "?error=invalid_request"
//                            + "&error_description=redirect_uri%20is%20required"
//                            + state);
//            return new ResponseEntity(responseHeaders, HttpStatus.FOUND); // Http code on error is still a redirect
//        }
        if (parameters.client_id == null) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("location",
                    parameters.redirect_uri
                            + "?error=invalid_request"
                            + "&error_description=client_id%20is%20required"
                            + state);
            return new ResponseEntity(responseHeaders, HttpStatus.FOUND); // Http code on error is still a redirect
        }
        if (parameters.response_type == null) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("location",
                    parameters.redirect_uri
                            + "?error=invalid_request"
                            + "&error_description=response_type%20is%20required"
                            + state);
            return new ResponseEntity(responseHeaders, HttpStatus.FOUND); // Http code on error is still a redirect
        }

        if (parameters.response_type.equalsIgnoreCase("code")) { // AuthorizationCode Flow Step 1
            return doAuthorizeCodeOrToken(parameters, request, authToken);
        } else if (parameters.response_type.equalsIgnoreCase("token")) { // Implicit Flow
            return doAuthorizeCodeOrToken(parameters, request, authToken);
        } else if (parameters.response_type.equalsIgnoreCase("jwt")) { // Jwt Flow
            return doAuthorizeJwt(parameters);
        } else {
            return new ResponseEntity("unsupported_response_type", HttpStatus.BAD_REQUEST);
        }
    }

    // TODO: this...
    protected ResponseEntity<Void> doAuthorizeJwt(AuthorizeParameters parameters) {
        return null;
    }


    protected ResponseEntity doAuthorizeCodeOrToken(AuthorizeParameters parameters, HttpServletRequest request, String authToken) {

        HttpHeaders responseHeaders = new HttpHeaders();
        String state = parameters.state;

        Client client = clientService.findClient(parameters.client_id);

        // check the client id
        if (client == null) {
            responseHeaders.add("location",
                    parameters.redirect_uri
                            + "?error=access_denied"
                            + "&error_description=bad_client_id"
                            + state);
            return new ResponseEntity(responseHeaders, HttpStatus.FOUND); // Http code on error is still a redirect
        }

        // check the resource matches URL
        if (!client.getClientRedirectUrl().equalsIgnoreCase(parameters.redirect_uri)) {
            responseHeaders.add("location",
                    parameters.redirect_uri
                            + "?error=access_denied"
                            + "&error_description=redirect_uri_dont_match"
                            + state);
            return new ResponseEntity(responseHeaders, HttpStatus.FOUND); // Http code on error is still a redirect
        }

        // we need to show login page, unless we are already logged in
        String username = isLoggedIn(authToken);//request);
        if (username == null) {
        	System.out.println();
            responseHeaders.add("location",
                    "/user/login"
                            + "?client_id=" + parameters.client_id
                            + "&redirect_uri=" + parameters.redirect_uri
                            + "&response_type=" + parameters.response_type
                            + "&scope=" + parameters.scope
                            + state);
            return new ResponseEntity(responseHeaders, HttpStatus.FOUND);
        }

        // TODO: possibly show an authorize page if it hasnt been requested before

        // check if scope is allowed (both on resource and allow the the user has given the permission)
        if (!client.getAllowedScopes().contains(parameters.scope)) {
            responseHeaders.add("location",
                    parameters.redirect_uri
                            + "?error=invalid_scope"
                            + "&error_description=The%20requested%20scope%20is%20invalid%20unknown%20or%20malformed"
                            + state);
            return new ResponseEntity(responseHeaders, HttpStatus.FOUND); // Http code on error is still a redirect
        }

        if (parameters.response_type.equalsIgnoreCase("code")) {

            if (!client.getFlow().toString().equalsIgnoreCase("AuthorizationCode")) {
                responseHeaders.add("location",
                        parameters.redirect_uri
                                + "?error=unauthorized_client"
                                + "&error_description=The%20client%20is%20not%20authorized%20to%20request%20an%20authorization%20code%20using%20this%20method"
                                + state);
                return new ResponseEntity(responseHeaders, HttpStatus.FOUND); // Http code on error is still a redirect
            } else {

                AuthorizationCode authorizationCode = authorizationCodeService.createAuthorizationCode(parameters.client_id, username);
                responseHeaders.add("location",
                        parameters.redirect_uri
                                + "?code=" + authorizationCode.getAuthorizationCode()
                                + state);
                return new ResponseEntity(responseHeaders, HttpStatus.FOUND);
            }

        } else if (parameters.response_type.equalsIgnoreCase("token")) {

            if (!client.getFlow().toString().equalsIgnoreCase("implicit")) {
                responseHeaders.add("location",
                        parameters.redirect_uri
                                + "?error=unauthorized_client"
                                + "&error_description=The%20client%20is%20not%20authorized%20to%20request%20an%20authorization%20code%20using%20this%20method"
                                + state);
                return new ResponseEntity(responseHeaders, HttpStatus.FOUND); // Http code on error is still a redirect
            } else {

                AccessToken accessToken = accessTokenService.createAccessToken(parameters.client_id, username, parameters.scope);

                // TODO: If the resource owner grants the access request, the authorization
                // server issues an access token and delivers it to the client by adding
                // the following parameters to the fragment component of the redirection
                // URI using the "application/x-www-form-urlencoded" format, per Appendix B
                responseHeaders.add("location",
                        parameters.redirect_uri
                                + "?access_token=" + accessToken.getAccessToken()
                                + "&token_type=" + accessToken.getTokenType()
                                + "&expires_in=" + accessToken.getExpiration()
                                + "&scope=" + accessToken.getScope()
                                + state);
                return new ResponseEntity(responseHeaders, HttpStatus.FOUND);
            }
        }

        // we shouldn't ever get here
        responseHeaders.add("location",
                parameters.redirect_uri
                        + "?error=server_error"
                        + "&error_description=bad_juju"
                        + state);
        return new ResponseEntity(responseHeaders, HttpStatus.FOUND); // Http code on error is still a redirect
    }

// this is loggged in wasn't working because cookies were never being saved,
// but also we just use the built in cookies now, not custom ones
//    protected String isLoggedIn(HttpServletRequest request) {
//        boolean loggedIn = true;
//
//        if (request.getCookies() != null) {
//            System.out.println("found some cookies!");
//
//            Cookie cookieValue = null;
//            Cookie[] cookies = request.getCookies();
//
//            for (int i = 0; i < cookies.length; i++) {
//                if (cookies[i].getName().equals("Auth-Token")) {
//                    cookieValue = cookies[i];
//                    break;
//                }
//            }
//
//            if (cookieValue != null) {
//                com.oauth.authorization.domain.Cookie cookie = cookieService.findByCookie(cookieValue.getValue());
//                if (cookie != null) {
//                    return cookie.getUsername();
//                } else {
//                    return null;
//                }
//            } else {
//                return null;
//            }
//
//        } else {
//            return null;
//        }
//    }
    
    protected String isLoggedIn(String authToken) {
    	String username = null;
    	if (!authToken.isEmpty()) {
    		username = atm.getUserFromToken(authToken).getUsername();
    	}
    	return username;
    }
}

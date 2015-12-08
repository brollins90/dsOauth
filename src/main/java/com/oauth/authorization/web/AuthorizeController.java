package com.oauth.authorization.web;

import com.oauth.authorization.domain.AccessToken;
import com.oauth.authorization.domain.AuthorizationCode;
import com.oauth.authorization.domain.Client;
import com.oauth.authorization.service.AccessTokenService;
import com.oauth.authorization.service.AuthorizationCodeService;
import com.oauth.authorization.service.ClientService;
import com.oauth.authorization.service.CookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private CookieService cookieService;


    @RequestMapping(path = "/oauth/authorize", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity authorize(
            @RequestParam(value = "response_type", required = true) String response_type,
            @RequestParam(value = "client_id", required = true) String client_id,
            @RequestParam(value = "redirect_uri", required = false) String redirect_uri,
            @RequestParam(value = "scope", required = false) String scope,
            @RequestParam(value = "state", required = false) String state,
            HttpServletRequest request) {

        AuthorizeParameters parameters = new AuthorizeParameters();
        parameters.setClientId(client_id);
        parameters.setRedirectUri(redirect_uri);
        parameters.setResponseType(response_type);
        parameters.setScope(scope);
        parameters.setState(state);

        if (parameters.getResponseType().equalsIgnoreCase("code")) { // AuthorizationCode Flow Step 1
            return doAuthorizeCodeOrToken(parameters, request);
        } else if (parameters.getResponseType().equalsIgnoreCase("token")) { // Implicit Flow
            return doAuthorizeCodeOrToken(parameters, request);
        } else if (parameters.getResponseType().equalsIgnoreCase("jwt")) { // Jwt Flow
            return doAuthorizeJwt(parameters);
        } else {
            return new ResponseEntity("Invalid response_type", HttpStatus.BAD_REQUEST);
        }
    }

    // TODO: this...
    protected ResponseEntity<Void> doAuthorizeJwt(AuthorizeParameters parameters) {
        return null;
    }


    protected ResponseEntity doAuthorizeCodeOrToken(AuthorizeParameters parameters, HttpServletRequest request) {

        HttpHeaders responseHeaders = new HttpHeaders();
        String state = (parameters.getState() != null) ? "&state=" + parameters.getState() : "";

        Client client = clientService.findClient(parameters.getClientId());

        // check the client id
        if (client == null) {
            responseHeaders.add("location", parameters.getRedirectUri() + "?error=access_denied" + state + "?error_description=bad_client_id");
            return new ResponseEntity(responseHeaders, HttpStatus.FORBIDDEN);
        }

        // check the resource matches URL
        if (!client.getClientRedirectUrl().equalsIgnoreCase(parameters.getRedirectUri())) {
            responseHeaders.add("location", parameters.getRedirectUri() + "?error=access_denied" + state + "?error_description=url_dont_match");
            return new ResponseEntity(responseHeaders, HttpStatus.FORBIDDEN);
        }

        // we need to show login page, unless we are already logged in
        String username = isLoggedIn(request);
        if (username == null) {
            responseHeaders.add("location", "/user/login" + "?redirect_uri=" + parameters.getRedirectUri() + state);
            return new ResponseEntity(responseHeaders, HttpStatus.FOUND);
        }

        // TODO: possibly show an authorize page if it hasnt been requested before

        // check if scope is allowed (both on resource and allow the the user has given the permission)
        if (!client.getAllowedScopes().contains(parameters.getScope())) {
            responseHeaders.add("location", parameters.getRedirectUri() + "?error=access_denied" + state + "?error_description=client_doesnt_have_this_scope");
            return new ResponseEntity(responseHeaders, HttpStatus.FORBIDDEN);
        }

        if (parameters.getResponseType().equalsIgnoreCase("code")) {

            if (!client.getFlow().toString().equalsIgnoreCase("code")) {
                responseHeaders.add("location", parameters.getRedirectUri() + "?error=access_denied" + state + "?error_description=client_doest_get_to_do_this_flow");
                return new ResponseEntity(responseHeaders, HttpStatus.FORBIDDEN);
            } else {

                AuthorizationCode authorizationCode = authorizationCodeService.createAuthorizationCode(parameters.getClientId(), username);
                responseHeaders.add("location", parameters.getRedirectUri() + "?code=" + authorizationCode.getAuthorizationCode() + state);
                return new ResponseEntity(responseHeaders, HttpStatus.FOUND);
            }

        } else if (parameters.getResponseType().equalsIgnoreCase("token")) {

            if (!client.getFlow().toString().equalsIgnoreCase("implicit")) {
                responseHeaders.add("location", parameters.getRedirectUri() + "?error=access_denied" + state + "?error_description=client_doest_get_to_do_this_flow");
                return new ResponseEntity(responseHeaders, HttpStatus.FORBIDDEN);
            } else {

                AccessToken accessToken = accessTokenService.createAccessToken(parameters.getClientId(), username, parameters.getScope());

                // TODO: If the resource owner grants the access request, the authorization
                // server issues an access token and delivers it to the client by adding
                // the following parameters to the fragment component of the redirection
                // URI using the "application/x-www-form-urlencoded" format, per Appendix B
                responseHeaders.add("location",
                        parameters.getRedirectUri() +
                                "?access_token=" + accessToken.getAccessToken() +
                                "&token_type=" + accessToken.getTokenType() +
                                "&expires_in=" + accessToken.getExpiration() +
                                "&scope=" + accessToken.getScope() +
                                state);
                return new ResponseEntity(responseHeaders, HttpStatus.FOUND);
            }
        }


        // we shouldn't ever get here
        responseHeaders.add("location", parameters.getRedirectUri() + "?error=access_denied" + state + "?error_description=bad_juju");
        return new ResponseEntity(responseHeaders, HttpStatus.FORBIDDEN);
    }

    protected String isLoggedIn(HttpServletRequest request) {
        boolean loggedIn = true;

        if (request.getCookies() != null) {
            System.out.println("found some cookies!");

            Cookie cookieValue = null;
            Cookie[] cookies = request.getCookies();

            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("Auth-Token")) {
                    cookieValue = cookies[i];
                    break;
                }
            }

            if (cookieValue != null) {
                com.oauth.authorization.domain.Cookie cookie = cookieService.findByCookie(cookieValue.getValue());
                if (cookie != null) {
                    return cookie.getUsername();
                } else {
                    return null;
                }
            } else {
                return null;
            }

        } else {
            return null;
        }
    }
}

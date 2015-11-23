package com.oauth.authorization.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
public class OauthController {

    /**
    client_id -	string - Required. The client ID that you received from DigitalOcean when you registered.
    redirect_uri -	string	- Required. Must match the callback URL that you supplied during application registration. The callback URL where users will be sent after authorization.
    response_type -	string - Required. Must be set to "code" to request an authorization code.
    scope -	string	- Optional. If not provided, scope defaults to "read". The valid scopes are listed below.
    state - string	- Recommended. An unguessable random string, used to protect against request forgery attacks.
     */
    @RequestMapping(path = "/authorize", method = RequestMethod.GET)
    public String authorize(@RequestParam("client_id") String client_id,
                            @RequestParam("redirect_uri") String redirect_uri,
                            @RequestParam("response_type") String response_type) {
        return "authorize";
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
    public String generateToken(@RequestParam("grand_type") String grant_type,
                                @RequestParam("code") String code,
                                @RequestParam("client_id") String client_id,
                                @RequestParam("client_secret") String client_secret,
                                @RequestParam("redirect_uri") String redirect_uri) {
        return "generateToken";
    }
}

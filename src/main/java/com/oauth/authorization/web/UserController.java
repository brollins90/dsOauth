package com.oauth.authorization.web;

import com.oauth.authorization.domain.AccessToken;
import com.oauth.authorization.domain.User;
import com.oauth.authorization.service.AccessTokenService;
import com.oauth.authorization.service.ClientService;
import com.oauth.authorization.service.CookieService;
import com.oauth.authorization.service.UserAuthenticationTokenManager;
import com.oauth.authorization.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private CookieService cookieService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserAuthenticationTokenManager atm;
    
    @Autowired
    private ClientService clientService;

    @RequestMapping("/profile")
    public ResponseEntity view(String username, Model model, @CookieValue(value = "Auth-Token", defaultValue = "") String authToken) {
        HttpHeaders responseHeaders = new HttpHeaders();
        if(!authToken.isEmpty()) {
            String loggedInUserName = atm.getUserFromToken(authToken).getUsername();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
            model.addAttribute("username", username);


            if(loggedInUserName.equals(username)) {
                return new ResponseEntity("profile", HttpStatus.OK);
            } else {
                responseHeaders.add("location", "http://localhost:8080/user/login2");
                return new ResponseEntity(responseHeaders, HttpStatus.FORBIDDEN); //trying to access someone else's profile
            }
        } else {
            return new ResponseEntity(responseHeaders, HttpStatus.UNAUTHORIZED); //no one is logged in
        }
    }

    @RequestMapping("/edit")
    public String edit(
            String username,
            User user,
            Model model) {
        userService.updateUser(user, username);
        return "redirect:profile?username=" + username;
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        return "newUser";
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String newUser(Model model, User user, HttpServletResponse response) {
        userService.addUser(user);
        String authToken = atm.generateAuthToken(user);
        //com.oauth.authorization.domain.Cookie ourCookie = cookieService.createCookie(user.getUsername());
        //Cookie authCookie = new Cookie("Auth-Token", ourCookie.getCookie());
        Cookie authCookie = new Cookie("Auth-Token", authToken);
        authCookie.setPath("/");
        response.addCookie(authCookie);
        return "redirect:profile?username=" + user.getUsername();
    }

    @RequestMapping(value = "/permissions", method = RequestMethod.GET) // TODO:
    public String permissionsList(String username, Model model) {
        User user = userService.findByUsername(username);
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("username", username);

            return "profile";
        } else {
            return "no user found";
        }
    }

    @RequestMapping(value = "/addpermission", method = RequestMethod.GET) // TODO:
    public ResponseEntity permissionAdd(
    		@CookieValue(value = "Auth-Token", defaultValue = "") String authToken,
    		String username, 
    		String client_id, 
    		String scope, 
    		String response_type, 
    		String redirect_uri, 
    		String state, 
    		Model model) {
        HttpHeaders responseHeaders = new HttpHeaders();
        if(!authToken.isEmpty()) {
            String loggedInUserName = atm.getUserFromToken(authToken).getUsername();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
            model.addAttribute("username", username);
            model.addAttribute("scope", scope);
            model.addAttribute("client", clientService.findClient(client_id).getClientName());

            if(loggedInUserName.equals(username)) {
                return new ResponseEntity("addPermission", HttpStatus.OK);
            } else {
                responseHeaders.add("location", "http://localhost:8080/user/login2");
                return new ResponseEntity(responseHeaders, HttpStatus.FORBIDDEN); //trying to access someone else's profile
            }
        } else {
            return new ResponseEntity(responseHeaders, HttpStatus.UNAUTHORIZED); //no one is logged in
        }
    }

    @RequestMapping(value = "/addpermission", method = RequestMethod.POST) // TODO:
    public String permissionAddPost(String username, Model model) {
//        User user = userService.findByUsername(username);
//        if (user != null) {
//            model.addAttribute("user", user);
//            model.addAttribute("username", username);
//
//            return "profile";
//        } else {
//            return "no user found";
//        }
        return "Not Yet Implemented exception :)";
    }

//    @RequestMapping("/editProfile")
//    public String edit(
//            String username,
//            User user,
//            Model model) {
//        db.updateUser(username, user);
//        return "redirect:profile?username=" + username;
//    }

    //login through a seperate client
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity loginUser(@ModelAttribute("username") String username,
                                    @ModelAttribute("password") String password,
                                    @ModelAttribute("redirect_uri") String redirect_uri,
                                    @ModelAttribute("response_type") String response_type,
                                    @ModelAttribute("client_id") String client_id,
                                    @ModelAttribute("scope") String scope,
                                    @ModelAttribute("state") String state,
                                    WebRequest request, HttpServletResponse response, Model model) {

        if(redirect_uri.equals("")) {
            return loginUserDirect(username, password, request, response, model );
        }
        Map<String, Object> returnModel = new HashMap<>();

        System.out.println("redirect_uri: " + redirect_uri);
        User user = userService.findByUsernameAndPassword(username, password);
        //User user = userService.findByUsername(username);

        //TODO: Fix the redirects. If the user logs in, redirect them to the correct page, not their profile.


        HttpHeaders responseHeaders = new HttpHeaders();

        if (user != null) {
            System.out.println("user: " + user.getUsername());

            String authToken = atm.generateAuthToken(user);
            //com.oauth.authorization.domain.Cookie ourCookie = cookieService.createCookie(user.getUsername());
            //Cookie authCookie = new Cookie("Auth-Token", ourCookie.getCookie());
            Cookie authCookie = new Cookie("Auth-Token", authToken);
            authCookie.setPath("/");
            response.addCookie(authCookie);
            model.addAttribute("user", user);

            // here we need to check if this user has given this client permissions before
            Iterable<AccessToken> iterableTokens = accessTokenService.findByUsername(username);
            AccessToken tokenForThisClient = null;
            for (AccessToken t : iterableTokens) {
                if (t.getUsername().equalsIgnoreCase(username) && t.getClientId().equalsIgnoreCase(client_id)) {
                    tokenForThisClient = t;
                }
            }

            if (tokenForThisClient == null) {
                // show permission page
                responseHeaders.add("location", "http://localhost:8080/user/addpermission?username=" + user.getUsername() + "&client_id=" + client_id + "&scope=" + scope + "&response_type=" + response_type + "&redirect_uri=" + redirect_uri + "&state=" + state);
                return new ResponseEntity(responseHeaders, HttpStatus.FOUND);
            } else {
                if (tokenForThisClient.getScope().equalsIgnoreCase(scope)) {
                    // redirect because the permission has already been granted
                    responseHeaders.add("location", "http://localhost:8080/oauth/authorize?client_id=" + client_id + "&scope=" + scope + "&response_type=" + response_type + "&redirect_uri=" + redirect_uri + "&state=" + state);
                    return new ResponseEntity(responseHeaders, HttpStatus.FOUND);
                } else {
                    // we have given this client some permissions, but not the one they are asking for.
                    // do we prompt to change permissions or deny???
                    responseHeaders.add("location", "http://localhost:8080/user/addpermission?client_id=" + client_id + "&scope=" + scope + "&response_type=" + response_type + "&redirect_uri=" + redirect_uri + "&state=" + state);
                    return new ResponseEntity(responseHeaders, HttpStatus.FOUND);
                }
            }


        } else {
            System.out.println("User not found: " + username);
            //returnModel.put("error", "User does not exist");
            model.addAttribute("error", "User does not exist");
            responseHeaders.add("location", "http://localhost:8080/oauth/authorize?client_id=" + client_id + "&scope=" + scope + "&response_type=" + response_type + "&redirect_uri=" + redirect_uri + "&state=" + state);
            //responseHeaders.add("location", "/user/login?redirect_uri=" + redirect_uri);
            return new ResponseEntity(responseHeaders, HttpStatus.FOUND);
        }
    }

    //login through the oauth server
    //@RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity loginUserDirect(@ModelAttribute("username") String username,
                                    @ModelAttribute("password") String password,
                                    WebRequest request, HttpServletResponse response, Model model) {

        Map<String, Object> returnModel = new HashMap<>();


        User user = userService.findByUsernameAndPassword(username, password);

        //TODO: Fix the redirects. If the user logs in, redirect them to the correct page, not their profile.


        HttpHeaders responseHeaders = new HttpHeaders();

        if (user != null) {
            System.out.println("user: " + user.getUsername());


            String authToken = atm.generateAuthToken(user);
            //com.oauth.authorization.domain.Cookie ourCookie = cookieService.createCookie(user.getUsername());
            //Cookie authCookie = new Cookie("Auth-Token", ourCookie.getCookie());
            Cookie authCookie = new Cookie("Auth-Token", authToken);
            authCookie.setPath("/");
            response.addCookie(authCookie);
            model.addAttribute("user", user);

            return new ResponseEntity(responseHeaders, HttpStatus.FOUND);
        } else {
            System.out.println("User not found: " + username);
            model.addAttribute("error", "User does not exist");
            responseHeaders.add("location", "http://localhost:8080/user/login");
            return new ResponseEntity(responseHeaders, HttpStatus.FOUND);
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLoginPage(Model model,
                                @CookieValue(value = "Auth-Token", defaultValue = "") String authToken,
                                @RequestParam(value = "client_id", required = true) String client_id,
                                @RequestParam(value = "redirect_uri", defaultValue = "http://localhost:8080/user/profile") String redirect_uri,
                                @RequestParam(value = "response_type", required = true) String response_type,
                                @RequestParam(value = "scope", required = false) String scope,
                                @RequestParam(value = "state", required = false) String state) {


        if (!authToken.isEmpty()) {
           // com.oauth.authorization.domain.Cookie cookie = cookieService.findByCookie(authToken);
           // if (cookie != null) {
                User user = userService.findByUsername(atm.getUserFromToken(authToken).getUsername());

                model.addAttribute("user", user);
            //}
        }
        AuthorizeParameters parameters = new AuthorizeParameters();
        parameters.client_id = client_id;
        parameters.redirect_uri = redirect_uri;
        parameters.response_type = response_type;
        parameters.scope = scope;
        parameters.state = state;
        model.addAttribute("authParams", parameters);
        return "login";
    }

    @RequestMapping(value = "/login2", method = RequestMethod.GET)
    public String showLoginPage() {
        return "loginclean";
    }

//    @RequestMapping(value = "/permission", method = RequestMethod.GET)
//    public ResponseEntity loginUser(@ModelAttribute("username") String username,
//                                    @ModelAttribute("password") String password,
//                                    @ModelAttribute("redirect_uri") String redirect_uri,
//                                    @ModelAttribute("response_type") String response_type,
//                                    @ModelAttribute("client_id") String client_id,
//                                    @ModelAttribute("scope") String scope,
//                                    @ModelAttribute("state") String state,
//                                    WebRequest request, HttpServletResponse response, Model model) {
//
//        Map<String, Object> returnModel = new HashMap<>();
//
//        System.out.println("redirect_uri: " + redirect_uri);
//        User user = userService.findByUsernameAndPassword(username, password);
//        //User user = userService.findByUsername(username);
//
//        //TODO: Fix the redirects. If the user logs in, redirect them to the correct page, not their profile.
//
//
//        HttpHeaders responseHeaders = new HttpHeaders();
//
//        if (user != null) {
//            System.out.println("user: " + user.getUsername());
//
//            //String authToken = atm.generateAuthToken(user);
//            com.oauth.authorization.domain.Cookie ourCookie = cookieService.createCookie(user.getUsername());
//            Cookie authCookie = new Cookie("Auth-Token", ourCookie.getCookie());
//            authCookie.setPath("/");
//            response.addCookie(authCookie);
//            model.addAttribute("user", user);
//            //returnModel.put("user", user);
//            // model.asMap().clear();
//            responseHeaders.add("location", "http://localhost:8080/oauth/authorize?client_id="+client_id+"&scope="+scope+"&response_type="+response_type+"&redirect_uri="+redirect_uri+"&state="+state);
//            return new ResponseEntity(responseHeaders, HttpStatus.FOUND);
//        } else {
//            System.out.println("User not found: " + username);
//            //returnModel.put("error", "User does not exist");
//            model.addAttribute("error", "User does not exist");
//            responseHeaders.add("location", "http://localhost:8080/oauth/authorize?client_id="+client_id+"&scope="+scope+"&response_type="+response_type+"&redirect_uri="+redirect_uri+"&state="+state);
//            //responseHeaders.add("location", "/user/login?redirect_uri=" + redirect_uri);
//            return new ResponseEntity(responseHeaders, HttpStatus.FOUND);
//        }
//    }

}

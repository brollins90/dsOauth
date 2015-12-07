package com.oauth.authorization.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import com.oauth.authorization.model.AuthorizationDB;
import com.oauth.authorization.model.implementation.*;

@Controller
@RequestMapping("/2/login")
public class LoginController {
	private static User defaultUser = new User();
	private static Client defaultClient = new Client();

    @Autowired
    AuthorizationDB db;

//    @Autowired
//    private UserAuthenticationTokenManager atm;

    @RequestMapping("/profile")
    public String profile(String username, Model model) {
        User user = db.getUser(username);
        model.addAttribute("user", user);
        model.addAttribute("username", username);
        return "profile";
    }

    @RequestMapping("/editProfile")
    public String edit(
            String username,
            User user,
            Model model) {
        db.updateUser(username, user);
        return "redirect:profile?username=" + username;
    }
    
    @RequestMapping(value="/newUser", method=RequestMethod.GET)
    public String newUser(Model model) {
    	model.addAttribute("user",defaultUser);
        return "newUser";
    }
    
    @RequestMapping(value="/newUser", method=RequestMethod.POST)
    public String newUser(Model model, User user) {
    	db.addUser(user);
    	return "redirect:profile?username=" + user.getUsername();
    }
    
    @RequestMapping("/client")
    public String client(String clientId, Model model) {
        Client client = db.getClient(clientId);
        model.addAttribute("client", client);
        return "client";
    }
    
    @RequestMapping("/editClient")
    public String editClient(
            Client client,
            Model model) {
        db.addClient(client);
        return "redirect:profile?username=" + client.getClientId();
    }
    
    @RequestMapping(value="/newClient", method=RequestMethod.GET)
    public String newClient(Model model) {
    	model.addAttribute("client",defaultClient);
        return "newClient";
    }
    
    @RequestMapping(value="/newClient", method=RequestMethod.POST)
    public String newClient(Model model, Client client) {
    	db.addClient(client);
    	return "redirect:client?clientId=" + client.getClientId();
    }

//    private boolean authenticate(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        boolean toBeContinued = true;
//
//        if (request.getCookies() != null) {
//            System.out.println("found some cookies!");
//
//            Cookie tokenCookie = null;
//            Cookie[] cookies = request.getCookies();
//
//            for (int i = 0; i < cookies.length; i++) {
//                if (cookies[i].getName().equals("Auth-Token")) {
//                    tokenCookie = cookies[i];
//                    break;
//                }
//            }
//
//            if (tokenCookie != null) {
//                if (!atm.validateAuthToken(tokenCookie.getValue())) {
//                    response.sendRedirect(request.getContextPath() + "/login");
//                    toBeContinued = false;
//                }
//            } else {
//                response.sendRedirect(request.getContextPath() + "/login");
//                toBeContinued = false;
//            }
//
//            // cookies["t"];
//        } else {
//            response.sendRedirect(request.getContextPath() + "/login");
//            toBeContinued = false;
//        }
//
//        return toBeContinued;
//    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity loginUser(@ModelAttribute("username") String username,
                                    @ModelAttribute("password") String password,
                                    @RequestParam("redirect_uri") String redirect_uri,
                                    WebRequest request, HttpServletResponse response, Model model) {

        Map<String, Object> returnModel = new HashMap<String, Object>();

        User user = db.getUser(username, password);

        //TODO: Fix the redirects. If the user logs in, redirect them to the correct page, not their profile.


        HttpHeaders responseHeaders = new HttpHeaders();

        if (user != null) {

            //String authToken = atm.generateAuthToken(user);
            Cookie authCookie = new Cookie("Auth-Token", user.getUsername());
            authCookie.setPath("/");
            response.addCookie(authCookie);
            model.addAttribute("user", user);
            //returnModel.put("user", user);
           // model.asMap().clear();
            responseHeaders.add("location", redirect_uri);
            return new ResponseEntity(responseHeaders, HttpStatus.TEMPORARY_REDIRECT);
        } else {
            //returnModel.put("error", "User does not exist");
            model.addAttribute("error", "User does not exist");
            responseHeaders.add("location", "/login");
            return new ResponseEntity(responseHeaders, HttpStatus.TEMPORARY_REDIRECT);
        }
    }
//
//    @RequestMapping(value = "/login", method = RequestMethod.GET)
//    public String showLoginPage(Model model, @CookieValue(value = "Auth-Token", defaultValue = "") String authToken) {
//
//        FakebookUser user = null;
//        if (!authToken.isEmpty()) {
//            user = atm.getUserFromToken(authToken);
//            model.addAttribute("user", user);
//        }
//        return "login";
//    }
}

package com.oauth.authorization.web;

import com.oauth.authorization.service.CookieService;
import com.oauth.authorization.service.UserService;
import com.oauth.authorization.domain.User;
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
    private CookieService cookieService;

    @Autowired
    private UserService userService;

    @RequestMapping("/profile")
    public String view(String username, Model model) {
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("username", username);
        return "profile";
    }

//    @RequestMapping("/editProfile")
//    public String edit(
//            String username,
//            User user,
//            Model model) {
//        db.updateUser(username, user);
//        return "redirect:profile?username=" + username;
//    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity loginUser(@ModelAttribute("username") String username,
                                    @ModelAttribute("password") String password,
                                    @RequestParam(value = "redirect_uri", defaultValue = "http://localhost:8080/user/profile") String redirect_uri,
                                    WebRequest request, HttpServletResponse response, Model model) {

        Map<String, Object> returnModel = new HashMap<String, Object>();

        User user = userService.findByUsernameAndPassword(username, password);

        //TODO: Fix the redirects. If the user logs in, redirect them to the correct page, not their profile.


        HttpHeaders responseHeaders = new HttpHeaders();

        if (user != null) {

            //String authToken = atm.generateAuthToken(user);
            com.oauth.authorization.domain.Cookie ourCookie = cookieService.createCookie(user.getUsername());
            Cookie authCookie = new Cookie("Auth-Token", ourCookie.getCookie());
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
            responseHeaders.add("location", "/user/login");
            return new ResponseEntity(responseHeaders, HttpStatus.TEMPORARY_REDIRECT);
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLoginPage(Model model, @CookieValue(value = "Auth-Token", defaultValue = "") String authToken) {

        if (!authToken.isEmpty()) {
            com.oauth.authorization.domain.Cookie cookie = cookieService.findByCookie(authToken);
            User user = userService.findByUsername(cookie.getUsername());

            model.addAttribute("user", user);
        }
        return "login";
    }
}

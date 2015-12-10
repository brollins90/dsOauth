package com.oauth.fakebookApplication.controller;

import com.oauth.authorization.service.UserAuthenticationTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import com.oauth.fakebookApplication.model.FakebookDB;
import com.oauth.fakebookApplication.model.implementation.FakebookUser;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AppController {

//    @Autowired
//    private FakebookDB db;
//
//    @Autowired
//    private UserAuthenticationTokenManager atm;
//
//    @RequestMapping("/profile")
//    public String view(int userId, Model model) {
//        FakebookUser user = db.getUser(userId);
//        model.addAttribute("user", user);
//        model.addAttribute("userId", userId);
//        return "profile";
//    }
//
//    @RequestMapping("/editProfile")
//    public String edit(
//            int userId,
//            FakebookUser user,
//            Model model) {
//        db.updateUser(userId, user);
//        return "redirect:profile?userId=" + userId;
//    }
//
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
//
//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    public String loginUser(@ModelAttribute("username") String username,
//                                  @ModelAttribute("password") String password,
//                                  WebRequest request, HttpServletResponse response, Model model) {
//
//        Map<String, Object> returnModel = new HashMap<String, Object>();
//
//        FakebookUser user = db.getUser(username, password);
//
//        //TODO: Fix the redirects. If the user logs in, redirect them to the correct page, not their profile.
//
//
//        if (user != null) {
//            String authToken = atm.generateAuthToken(user);
//            Cookie authCookie = new Cookie("Auth-Token", authToken);
//            authCookie.setPath("/");
//            response.addCookie(authCookie);
//            model.addAttribute("user", user);
//            //returnModel.put("user", user);
//           // model.asMap().clear();
//            return "redirect:/profile?userId="+user.getUserId();
//        } else {
//            //returnModel.put("error", "User does not exist");
//            model.addAttribute("error", "User does not exist");
//            return "login";
//        }
//    }
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

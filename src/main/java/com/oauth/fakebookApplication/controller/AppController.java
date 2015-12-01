package com.oauth.fakebookApplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.oauth.fakebookApplication.model.FakebookDB;
import com.oauth.fakebookApplication.model.implementation.FakebookUser;

@Controller
public class AppController {
	
	@Autowired
    private FakebookDB db;
	
	@RequestMapping("/profile")
	public String  view(int userId, Model model) {
		FakebookUser user = db.getUser(userId);
		model.addAttribute("user", user);
		model.addAttribute("userId", userId);
		return "profile";
	}
	
	@RequestMapping("/editProfile")
	public String  edit(
			int userId,
			FakebookUser user,
			Model model) {
		db.updateUser(userId, user);
		return "redirect:profile?userId=" + userId;
	}
}

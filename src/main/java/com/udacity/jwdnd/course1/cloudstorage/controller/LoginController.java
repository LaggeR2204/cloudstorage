package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String loginView() {
        return "login";
    }

    @GetMapping("/logout")
    public String logoutView(Model model) {
        model.addAttribute("isLogout", true);
        return "login";
    }
}

package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
public class SignupController {
    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String signupView() {
        return "signup";
    }

    @PostMapping
    public String createNewUser(@ModelAttribute User user, RedirectAttributes redirectAttributes, Model model) {
        String errorMsg = null;

        if (userService.isUsernameAlreadyExist(user.getUsername())) {
            errorMsg = "This username is already exist";
        } else {
            int rowAdded = userService.createNewUser(user);
            if (rowAdded < 0) {
                errorMsg = "Please try again. There was an error!!!";
            }
        }

        if (errorMsg == null) {
            model.addAttribute("signupSuccess", true);
            redirectAttributes.addFlashAttribute("signupSuccess", true);
            return "redirect:/login";
        } else {
            model.addAttribute("signupError", errorMsg);
        }

        return "signup";
    }
}

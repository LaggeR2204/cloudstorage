package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/credential")
public class CredentialController {
    private final UserService userService;
    private final CredentialService credentialService;

    public CredentialController(UserService userService, CredentialService credentialService) {
        this.userService = userService;
        this.credentialService = credentialService;
    }

    @PostMapping
    public String addNewCredential(@ModelAttribute Credential credential, Authentication authentication, Model model) {
        String errorMsg = null;
        User loggedInUser;

        try {
            loggedInUser = userService.getUserByUsername(authentication.getPrincipal().toString());
            if (loggedInUser == null) {
                return "redirect:/logout";
            }
        } catch (Exception e) {
            return "redirect:/logout";
        }

        try {
            if (credential.getCredentialId() != null) {
                // Update credential
                credentialService.updateCredential(new Credential(credential.getCredentialId(), credential.getUrl(), credential.getUsername(), null, credential.getPassword(), loggedInUser.getUserId()));
            } else {
                // Insert credential
                credentialService.addNewCredential(new Credential(null, credential.getUrl(), credential.getUsername(), null, credential.getPassword(), loggedInUser.getUserId()));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorMsg = "Please try again. There was an error!!!";
        }

        if (errorMsg == null) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("error", errorMsg);
        }

        return "result";
    }

    @GetMapping("/delete/{credentialId}")
    public String deleteCredential(@PathVariable Integer credentialId, Authentication authentication, Model model) {
        String errorMsg = null;
        User loggedInUser;

        try {
            loggedInUser = userService.getUserByUsername(authentication.getPrincipal().toString());
            if (loggedInUser == null) {
                return "redirect:/logout";
            }
        } catch (Exception e) {
            return "redirect:/logout";
        }

        try {
            // delete credential
            credentialService.deleteCredential(credentialId, loggedInUser.getUserId());
        } catch (Exception e) {
            errorMsg = "Please try again. There was an error!!!";
        }

        if (errorMsg == null) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("error", errorMsg);
        }

        return "result";
    }
}

package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final NoteService noteService;
    private final UserService userService;
    private final FileService fileService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;

    public HomeController(NoteService noteService, UserService userService, FileService fileService, CredentialService credentialService, EncryptionService encryptionService) {
        this.noteService = noteService;
        this.userService = userService;
        this.fileService = fileService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping
    public String homeView(Authentication authentication, Model model) {
        model.addAttribute("encryptionService", encryptionService);
        try {
            User loggedInUser = userService.getUserByUsername(authentication.getPrincipal().toString());

            List<Note> noteList = noteService.getNotesByUserId(loggedInUser.getUserId());
            model.addAttribute("noteList", noteList);

            List<File> fileList = fileService.getFilesByUserId(loggedInUser.getUserId());
            model.addAttribute("fileList", fileList);

            List<Credential> credentialList = credentialService.getAllCredentialsByUserId(loggedInUser.getUserId());
            model.addAttribute("credentialList", credentialList);
        } catch (Exception e) {
            return "redirect:/logout";
        }

        return "home";
    }
}

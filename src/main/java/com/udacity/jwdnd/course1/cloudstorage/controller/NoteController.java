package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/note")
public class NoteController {
    private final UserService userService;
    private final NoteService noteService;


    public NoteController(UserService userService, NoteService noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    @PostMapping()
    public String createOrUpdateNote(@ModelAttribute Note note, Authentication authentication, Model model) {
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
            if (note.getNoteId() != null) {
                // Update note
                noteService.updateNote(new Note(note.getNoteId(), note.getNoteTitle(), note.getNoteDescription(), loggedInUser.getUserId()));
            } else {
                // Insert note
                noteService.createNewNote(new Note(null, note.getNoteTitle(), note.getNoteDescription(), loggedInUser.getUserId()));
            }
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

    @GetMapping("/delete/{noteId}")
    public String deleteNote(@PathVariable Integer noteId, Authentication authentication, Model model) {
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
            // delete note
            noteService.deleteNoteByNoteId(noteId, loggedInUser.getUserId());
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

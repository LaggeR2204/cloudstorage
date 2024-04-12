package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.constants.AppConstants;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/file")
public class FileController {
    private final UserService userService;
    private final FileService fileService;

    public FileController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @PostMapping
    public String addNewFile(@RequestParam MultipartFile fileUpload, Authentication authentication, Model model) {
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

        // check the file is empty or not
        if (fileUpload.isEmpty()) {
            errorMsg = "There was an error!!! The file is empty. Please try again";
            model.addAttribute("error", errorMsg);
            return "result";
        }

        // check upload size is permitted or not
        if (fileUpload.getSize() >= AppConstants.MAX_FILE_UPLOAD_SIZE) {
            errorMsg = "There was an error!!! The size of file is too large.";
            model.addAttribute("error", errorMsg);
            return "result";
        }

        try {
            // check the file already exists or not
            File existedFileWithSameName = fileService.getFileByFileName(fileUpload.getOriginalFilename(), loggedInUser.getUserId());
            if (existedFileWithSameName == null) {
                fileService.addNewFile(new File(null, fileUpload.getOriginalFilename(), fileUpload.getContentType(), fileUpload.getSize(), loggedInUser.getUserId(), fileUpload.getBytes()));
            } else {
                errorMsg = "There was an error!!! There is a file with the same name";
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

    @GetMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable Integer fileId, Authentication authentication, Model model) {
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
            fileService.deleteFileByFileId(fileId, loggedInUser.getUserId());
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

    @GetMapping(value = "/view/{fileId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<ByteArrayResource> viewFile(@PathVariable Integer fileId, Authentication authentication) {
        User loggedInUser;

        try {
            loggedInUser = userService.getUserByUsername(authentication.getPrincipal().toString());
            if (loggedInUser == null) {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        try {
            File file = fileService.getFileByFileId(fileId, loggedInUser.getUserId());
            if (file != null) {
                return ResponseEntity.ok().contentType(MediaType.parseMediaType(file.getContentType())).body(new ByteArrayResource(file.getFileData()));
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.notFound().build();

    }
}

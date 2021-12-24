package com.t2.timetables.parser.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class RootController {
    @GetMapping("/")
    public RedirectView redirect() {
        return new RedirectView("/index.html");
    }

    @PostMapping("/")
    public ResponseEntity<MultipartFile> parse(@RequestParam MultipartFile input) {
        return ResponseEntity.status(HttpStatus.OK).body(input);
    }
}

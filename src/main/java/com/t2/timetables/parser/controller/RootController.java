package com.t2.timetables.parser.controller;

import com.t2.timetables.parser.service.ParsingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
public class RootController {
    @Autowired
    ParsingService parsingService;

    @GetMapping("/")
    public RedirectView redirect() {
        return new RedirectView("/index.html");
    }

    @PostMapping("/")
    public ResponseEntity<Resource> parse(@RequestParam MultipartFile input) throws IOException {
        parsingService.parse(input.getInputStream());
        return ResponseEntity
                .ok()
                .contentLength(input.getSize())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(input.getInputStream()));
    }
}

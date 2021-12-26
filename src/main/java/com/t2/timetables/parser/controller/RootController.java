package com.t2.timetables.parser.controller;

import com.t2.timetables.parser.service.ParsingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
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
    public void convert(HttpServletResponse response, @RequestParam MultipartFile[] files) throws IOException {
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"converted.zip\"");
        parsingService.convertMultiple(files, response.getOutputStream());
    }
}

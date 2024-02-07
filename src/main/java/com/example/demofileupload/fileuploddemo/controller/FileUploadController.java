package com.example.demofileupload.fileuploddemo.controller;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jersey.JerseyProperties.Servlet;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demofileupload.fileuploddemo.service.FileUploadService;

@RestController
public class FileUploadController {

    @Autowired
    private FileUploadService service;
    private static final String UPLOAD_DIR = "C:/Users/user/Desktop/uploads";

    @PostMapping("/upload")
    public ResponseEntity<String> uplodFile(@RequestParam("file") MultipartFile multipartFile) {
        ResponseEntity<String> response = service.storeFile(multipartFile);
        String fileDownloadUri = response.getBody();
        return ResponseEntity.ok().body("File uploaded successfully. Download link: " + fileDownloadUri);
    }

    @PostMapping("/uploadMultiple")
    public ResponseEntity<String> uploadMultipleFiles(@RequestParam("files") MultipartFile[] multipartFile) {
        ResponseEntity<String> response = service.storeMultipleFiles(multipartFile);
        return ResponseEntity.ok().body(response.getBody());

    }

    @PostMapping("/append/{fileName}")
    public ResponseEntity<String> appendToFile(@PathVariable String fileName,
            @RequestParam("content") String content) {
        ResponseEntity<String> response = service.appendToFile(fileName, content);
        return ResponseEntity.ok().body(response.getBody());
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Object> downloadFile(@PathVariable String fileName) {
        return service.downloadFile(fileName);
    }
}
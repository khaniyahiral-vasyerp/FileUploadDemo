package com.example.demofileupload.fileuploddemo.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    ResponseEntity<String> storeFile(MultipartFile file);

    ResponseEntity<String> storeMultipleFiles(MultipartFile[] files);

    ResponseEntity<String> appendToFile(String fileName, String content);

    ResponseEntity<Object> downloadFile(String fileName);

}

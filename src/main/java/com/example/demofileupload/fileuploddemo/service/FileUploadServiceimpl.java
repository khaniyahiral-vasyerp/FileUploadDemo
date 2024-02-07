package com.example.demofileupload.fileuploddemo.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileUploadServiceimpl implements FileUploadService {
    private static final String UPLOAD_DIR = "C:\\Users\\hiralkhaniya\\Downloads";

    @Override
    public ResponseEntity<String> storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // create the directory if it doesn't exist
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            // set the file path
            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName);
            // creating the file
            Files.write(filePath, file.getBytes(), StandardOpenOption.CREATE);
            // download the uri
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/download/")
                    .path(fileName)
                    .toUriString();

            return ResponseEntity.ok().body("File upload sucessfully " + ":" + fileDownloadUri);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not store file " + fileName + ". Please try again!");
        }
    }

    // uploading multiple file
    @Override
    public ResponseEntity<String> storeMultipleFiles(MultipartFile[] files) {
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile multipartFile : files) {
            String fileName = storeFile(multipartFile).getBody();
            fileNames.add(fileName);
        }
        return ResponseEntity.ok().body("file uploaded sucessfully  . upload files" + fileNames);
    }

    // appending file
    @Override
    public ResponseEntity<String> appendToFile(String fileName, String content) {
        Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).normalize();
        try {
            Files.write(filePath, content.getBytes(), StandardOpenOption.APPEND);
            return ResponseEntity.ok().body("file appended sucessfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
        }
    }

    @SuppressWarnings("null")
    @Override
    public ResponseEntity<Object> downloadFile(String fileName) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).normalize();
            if (Files.exists(filePath)) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", fileName);
                return ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(Files.size(filePath))
                        .body(new FileSystemResource(filePath.toFile()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Failed to download the file: " + ex.getMessage());
        }
    }
}
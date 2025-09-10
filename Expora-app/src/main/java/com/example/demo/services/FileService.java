package com.example.demo.services;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    // General upload method for any file type
    String uploadFile(String path, MultipartFile file) throws IOException;

    // Get resource stream by path and filename
    InputStream getResource(String path, String fileName) throws IOException;
}

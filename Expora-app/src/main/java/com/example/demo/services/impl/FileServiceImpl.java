package com.example.demo.services.impl;

import com.example.demo.services.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadFile(String uploadDir, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("File is empty or null");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new IOException("Invalid file name: " + originalFilename);
        }

        // Extract file extension
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String randomFileName = UUID.randomUUID() + extension;

        // Ensure directory exists
        File directory = new File(uploadDir);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Failed to create directory: " + uploadDir);
        }

        File destinationFile = new File(directory, randomFileName);
        file.transferTo(destinationFile);

        log.info("File uploaded to: {}", destinationFile.getAbsolutePath());
        return randomFileName;
    }

    @Override
    public InputStream getResource(String uploadDir, String fileName) throws IOException {
        String fullPath = Paths.get(uploadDir, fileName).normalize().toString();
        File file = new File(fullPath);

        if (!file.exists() || !file.isFile()) {
            throw new FileNotFoundException("File not found at path: " + fullPath);
        }

        return new FileInputStream(file);
    }
}

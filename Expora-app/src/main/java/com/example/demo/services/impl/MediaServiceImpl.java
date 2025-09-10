package com.example.demo.services.impl;

import com.example.demo.entity.Media;
import com.example.demo.entity.Post;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.payloads.MediaDto;
import com.example.demo.repositries.MediaRepository;
import com.example.demo.repositries.PostRepo;
import com.example.demo.services.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MediaServiceImpl implements MediaService {

    private static final String UPLOAD_DIR = "uploads";
    private static final long MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024; // 10 MB

    private static final List<String> ALLOWED_EXTENSIONS = List.of(
            "jpg", "jpeg", "png", "gif",
            "mp4", "mov", "avi", "mkv",
            "pdf", "ppt", "pptx", "doc", "docx",
            "xls", "xlsx", "txt"
    );

    private final MediaRepository mediaRepository;
    private final PostRepo postRepository;
    private final ModelMapper modelMapper;

    public MediaServiceImpl(MediaRepository mediaRepository, PostRepo postRepository, ModelMapper modelMapper) {
        this.mediaRepository = mediaRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public MediaDto addMediaToPost(Integer postId, MediaDto mediaDto) {
        Post post = getPostOrThrow(postId);
        Media media = modelMapper.map(mediaDto, Media.class);
        media.setPost(post);
        Media saved = mediaRepository.save(media);
        log.info("Media added to post {}: {}", postId, saved.getUrl());
        return modelMapper.map(saved, MediaDto.class);
    }

    @Override
    public MediaDto uploadMediaFile(Integer postId, MultipartFile file, String mediaType, Integer displayOrder) {
        Post post = getPostOrThrow(postId);
        validateFile(file);

        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String filename = UUID.randomUUID() + "." + extension;

        Path uploadPath = Paths.get(UPLOAD_DIR);
        try {
            if (Files.notExists(uploadPath)) {
                Files.createDirectories(uploadPath);
                log.info("Created upload directory: {}", uploadPath.toAbsolutePath());
            }

            Path fullPath = uploadPath.resolve(filename);
            file.transferTo(fullPath.toFile());

            String mediaUrl = "/" + UPLOAD_DIR + "/" + filename;

            Media media = Media.builder()
                    .url(mediaUrl)
                    .mediaType(mediaType)
                    .displayOrder(displayOrder)
                    .post(post)
                    .build();

            Media saved = mediaRepository.save(media);
            log.info("Uploaded media file: {}", mediaUrl);

            return modelMapper.map(saved, MediaDto.class);

        } catch (IOException e) {
            log.error("File upload failed for {}", originalFilename, e);
            throw new RuntimeException("File upload failed. Try again later.");
        }
    }

    @Override
    public List<MediaDto> getMediaByPost(Integer postId) {
        List<Media> mediaList = mediaRepository.findByPostId(postId);
        return mediaList.stream()
                .map(media -> modelMapper.map(media, MediaDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMedia(Integer mediaId) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Media", "id", mediaId));

        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(Paths.get(media.getUrl()).getFileName());
            Files.deleteIfExists(filePath);
            log.info("Deleted media file from storage: {}", filePath);
        } catch (IOException e) {
            log.warn("File deletion failed: {}", media.getUrl(), e);
        }

        mediaRepository.delete(media);
        log.info("Media entity deleted from DB: ID {}", mediaId);
    }

    @Override
    public MediaDto updateMedia(Integer mediaId, MediaDto mediaDto) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Media", "id", mediaId));

        media.setMediaType(mediaDto.getMediaType());
        media.setDisplayOrder(mediaDto.getDisplayOrder());

        Media updated = mediaRepository.save(media);
        log.info("Updated media metadata: ID {}", mediaId);
        return modelMapper.map(updated, MediaDto.class);
    }

    @Override
    public MediaDto getMediaById(Integer mediaId) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Media", "id", mediaId));
        return modelMapper.map(media, MediaDto.class);
    }

    // 🔐 Private Helper Methods
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Uploaded file is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new RuntimeException("File exceeds max size of 10 MB");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new RuntimeException("Invalid file name");
        }

        String extension = getFileExtension(originalFilename);
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new RuntimeException("Unsupported file format: " + extension);
        }
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.') + 1);
    }

    private Post getPostOrThrow(Integer postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
    }
}

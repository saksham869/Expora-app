package com.example.demo.controllers;

import com.example.demo.payloads.MediaDto;
import com.example.demo.services.MediaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    private final MediaService mediaService;

    @Autowired
    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    /**
     * Upload a media file to a specific post.
     * 
     * @param postId ID of the post to upload media for
     * @param file Multipart file uploaded
     * @param mediaType Type of media (image, video, etc.) - optional, defaults to "image"
     * @param displayOrder The order in which this media should be displayed
     * @return MediaDto object representing the uploaded media
     */
    @PostMapping("/{postId}/upload")
    public ResponseEntity<MediaDto> uploadMediaToPost(
            @PathVariable Integer postId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "mediaType", defaultValue = "image", required = false) String mediaType,
            @RequestParam(value = "displayOrder", defaultValue = "0", required = false) Integer displayOrder) {

        MediaDto dto = mediaService.uploadMediaFile(postId, file, mediaType, displayOrder);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    /**
     * Get all media files attached to a specific post.
     * 
     * @param postId ID of the post
     * @return List of MediaDto for the post
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<MediaDto>> getMediaByPost(@PathVariable Integer postId) {
        List<MediaDto> mediaList = mediaService.getMediaByPost(postId);
        return ResponseEntity.ok(mediaList);
    }

    /**
     * Get a specific media by its ID.
     * 
     * @param mediaId ID of the media
     * @return MediaDto object
     */
    @GetMapping("/{mediaId}")
    public ResponseEntity<MediaDto> getMediaById(@PathVariable Integer mediaId) {
        MediaDto dto = mediaService.getMediaById(mediaId);
        return ResponseEntity.ok(dto);
    }

    /**
     * Delete a media file by its ID.
     * 
     * @param mediaId ID of the media to delete
     * @return HTTP 204 No Content if successful
     */
    @DeleteMapping("/{mediaId}")
    public ResponseEntity<Void> deleteMedia(@PathVariable Integer mediaId) {
        mediaService.deleteMedia(mediaId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Update metadata of a media file (except the actual file content).
     * 
     * @param mediaId ID of the media to update
     * @param mediaDto MediaDto containing updated metadata
     * @return Updated MediaDto
     */
    @PutMapping("/{mediaId}")
    public ResponseEntity<MediaDto> updateMedia(@PathVariable Integer mediaId,
                                                @RequestBody MediaDto mediaDto) {
        MediaDto updatedDto = mediaService.updateMedia(mediaId, mediaDto);
        return ResponseEntity.ok(updatedDto);
    }
}

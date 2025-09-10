package com.example.demo.services;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.payloads.MediaDto;

import java.util.List;

/**
 * Service interface for handling Media-related operations.
 */
public interface MediaService {

    /**
     * Adds media metadata to a post.
     * 
     * @param postId the ID of the post
     * @param mediaDto media data to add
     * @return the saved media data
     */
    MediaDto addMediaToPost(Integer postId, MediaDto mediaDto);

    /**
     * Uploads a media file and associates it with a post.
     *
     * @param postId the ID of the post
     * @param file the media file to upload
     * @param mediaType the type of media (image, video, etc.)
     * @param displayOrder order in which media should be displayed
     * @return the saved media data
     */
    MediaDto uploadMediaFile(Integer postId, MultipartFile file, String mediaType, Integer displayOrder);

    /**
     * Retrieves all media for a given post.
     * 
     * @param postId the ID of the post
     * @return list of media DTOs
     */
    List<MediaDto> getMediaByPost(Integer postId);

    /**
     * Deletes a media by its ID.
     * 
     * @param mediaId the ID of the media to delete
     */
    void deleteMedia(Integer mediaId);

    /**
     * Updates existing media metadata.
     * 
     * @param mediaId the ID of the media to update
     * @param mediaDto updated media data
     * @return the updated media DTO
     */
    MediaDto updateMedia(Integer mediaId, MediaDto mediaDto);

    /**
     * Retrieves media by its ID.
     * 
     * @param mediaId the ID of the media
     * @return media DTO
     */
    MediaDto getMediaById(Integer mediaId);
}

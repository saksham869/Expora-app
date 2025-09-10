package com.example.demo.repositries;

import com.example.demo.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface for Media entity.
 */
public interface MediaRepository extends JpaRepository<Media, Integer> {
    /**
     * Retrieves all media associated with a given post ID.
     *
     * @param postId the ID of the post
     * @return list of Media entities linked to the post
     */
    List<Media> findByPostId(Integer postId);
}

package com.example.demo.repositries;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Category;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;

public interface PostRepo extends JpaRepository<Post, Integer> {

    // Find posts created by a specific user
    List<Post> findByUser(User user);
    
    // Find posts belonging to a specific category
    List<Post> findByCategory(Category category);
    
    // Search posts where title or content contains the keyword (case-sensitive)
    @Query("SELECT p FROM Post p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Post> searchByKeyword(@Param("keyword") String keyword);

	List<Post> findByTitleContaining(String keyword);
    
    // Alternative Spring Data method that does the same without explicit @Query
    // List<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String titleKeyword, String contentKeyword);
}

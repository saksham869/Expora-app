package com.example.demo.repositries;

import com.example.demo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment, Integer> {
    // Custom query methods can be defined here if needed
}

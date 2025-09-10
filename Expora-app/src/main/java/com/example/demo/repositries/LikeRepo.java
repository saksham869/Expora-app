package com.example.demo.repositries;

import com.example.demo.entity.Like;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface LikeRepo extends JpaRepository<Like, Integer> {
    Optional<Like> findByPostAndUser(Post post, User user);
    List<Like> findByPost(Post post);
    Integer countByPost(Post post);
}

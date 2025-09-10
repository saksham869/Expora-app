package com.example.demo.controllers;

import com.example.demo.payloads.LikeDto;
import com.example.demo.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/post/{postId}/user/{userId}")
    public ResponseEntity<LikeDto> likePost(@PathVariable Integer postId, @PathVariable Integer userId) {
        return new ResponseEntity<>(likeService.likePost(postId, userId), HttpStatus.CREATED);
    }

    @DeleteMapping("/post/{postId}/user/{userId}")
    public ResponseEntity<Void> unlikePost(@PathVariable Integer postId, @PathVariable Integer userId) {
        likeService.unlikePost(postId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<LikeDto>> getLikes(@PathVariable Integer postId) {
        return ResponseEntity.ok(likeService.getLikesByPost(postId));
    }

    @GetMapping("/post/{postId}/count")
    public ResponseEntity<Integer> getLikeCount(@PathVariable Integer postId) {
        return ResponseEntity.ok(likeService.getLikeCount(postId));
    }

    @GetMapping("/post/{postId}/user/{userId}")
    public ResponseEntity<Boolean> isLiked(@PathVariable Integer postId, @PathVariable Integer userId) {
        return ResponseEntity.ok(likeService.isPostLikedByUser(postId, userId));
    }
}

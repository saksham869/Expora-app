package com.example.demo.services;

import com.example.demo.payloads.LikeDto;

import java.util.List;

public interface LikeService {
    LikeDto likePost(Integer postId, Integer userId);
    void unlikePost(Integer postId, Integer userId);
    List<LikeDto> getLikesByPost(Integer postId);
    Integer getLikeCount(Integer postId);
    boolean isPostLikedByUser(Integer postId, Integer userId);
}

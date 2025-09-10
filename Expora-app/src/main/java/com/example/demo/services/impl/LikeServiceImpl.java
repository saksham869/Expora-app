package com.example.demo.services.impl;

import com.example.demo.entity.*;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.payloads.LikeDto;
import com.example.demo.repositries.*;
import com.example.demo.services.LikeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeRepo likeRepo;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public LikeDto likePost(Integer postId, Integer userId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        likeRepo.findByPostAndUser(post, user).ifPresent(l -> {
            throw new RuntimeException("User already liked this post");
        });

        Like like = Like.builder().post(post).user(user).build();
        return modelMapper.map(likeRepo.save(like), LikeDto.class);
    }

    @Override
    public void unlikePost(Integer postId, Integer userId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        Like like = likeRepo.findByPostAndUser(post, user)
                .orElseThrow(() -> new ResourceNotFoundException("Like", "post/user", postId + "/" + userId));

        likeRepo.delete(like);
    }

    @Override
    public List<LikeDto> getLikesByPost(Integer postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));

        return likeRepo.findByPost(post).stream()
                .map(l -> modelMapper.map(l, LikeDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Integer getLikeCount(Integer postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));
        return likeRepo.countByPost(post);
    }

    @Override
    public boolean isPostLikedByUser(Integer postId, Integer userId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        return likeRepo.findByPostAndUser(post, user).isPresent();
    }
}

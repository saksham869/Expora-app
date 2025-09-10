package com.example.demo.services;

import java.util.List;

import com.example.demo.payloads.CommentDto;

public interface CommentService {
    CommentDto createComment(CommentDto commentDto, Integer postId);
    CommentDto createComment(CommentDto commentDto, Integer postId, Integer parentCommentId);
    void deleteComment(Integer commentId);
	List<CommentDto> getCommentsByPostId(Integer postId);
}

package com.example.demo.controllers;

import com.example.demo.payloads.CommentDto;
import com.example.demo.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // ✅ Create top-level comment
    @PostMapping("/post/{postId}")
    public ResponseEntity<CommentDto> createComment(
            @RequestBody CommentDto comment,
            @PathVariable Integer postId
    ) {
        CommentDto createdComment = commentService.createComment(comment, postId);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    // ✅ Create reply to another comment
    @PostMapping("/post/{postId}/reply/{parentCommentId}")
    public ResponseEntity<CommentDto> createReply(
            @RequestBody CommentDto comment,
            @PathVariable Integer postId,
            @PathVariable Integer parentCommentId
    ) {
        CommentDto createdReply = commentService.createComment(comment, postId, parentCommentId);
        return new ResponseEntity<>(createdReply, HttpStatus.CREATED);
    }

    // ✅ Delete a comment
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
    }

    // ✅ Get all comments by post
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDto>> getCommentsByPost(@PathVariable Integer postId) {
        List<CommentDto> comments = commentService.getCommentsByPostId(postId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}
